package kware.apps.manager.cetus.user.service;

import cetus.bean.Page;
import cetus.bean.Pageable;
import cetus.user.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import kware.apps.manager.cetus.dept.deptuser.service.CetusDeptUserService;
import kware.apps.manager.cetus.enumstatus.DownloadTargetCd;
import kware.apps.manager.cetus.enumstatus.UserAuthorCd;
import kware.apps.manager.cetus.enumstatus.UserStatus;
import kware.apps.manager.cetus.group.groupuser.service.CetusGroupUserService;
import kware.apps.manager.cetus.position.positionuser.service.CetusPositionUserService;
import kware.apps.manager.cetus.statushist.service.CetusUserStatusHistService;
import kware.apps.manager.cetus.user.domain.CetusUser;
import kware.apps.manager.cetus.user.domain.CetusUserDao;
import kware.apps.manager.cetus.user.dto.request.*;
import kware.apps.manager.cetus.user.dto.response.UserExcelPage;
import kware.apps.manager.cetus.user.dto.response.UserFullInfo;
import kware.apps.manager.cetus.user.dto.response.UserList;
import kware.apps.manager.cetus.user.dto.response.UserView;
import kware.apps.manager.cetus.workplace.workplaceuser.service.CetusWorkplaceUserService;
import kware.common.excel.ExcelCreate;
import kware.common.exception.SimpleException;
import kware.common.file.domain.CommonFile;
import kware.common.file.service.CommonFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CetusUserService {

    private final CetusUserStatusHistService statusHistService;
    private final CetusDeptUserService deptUserService;
    private final CetusGroupUserService groupUserService;
    private final CetusPositionUserService positionUserService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CetusUserDao dao;
    private final CommonFileService commonFileService;
    private final ExcelCreate excelCreate;
    private final CetusWorkplaceUserService workplaceUserService;

    @Transactional
    public void saveUser(UserSave request) {

        // 0. metaData 묶기
        String processedMetaData = this.processMetaData(request.getMetaData());

        // 1. user
        String encodePassword = passwordEncoder.encode(request.getPassword());
        CetusUser bean = new CetusUser(request, encodePassword, processedMetaData);
        dao.insert(bean);

        // 2. workplace
        workplaceUserService.saveWorkplaceUser(1L, bean.getUid());
    }

    @Transactional
    public void saveUserAdmin(UserSaveAdmin request) {

        // 0. metaData 묶기
        String processedMetaData = this.processMetaData(request.getMetaData());

        // 1. user
        String encodePassword = passwordEncoder.encode(request.getPassword());
        CetusUser bean = new CetusUser(request, encodePassword, processedMetaData);
        dao.insert(bean);

        // 2. workplace
        workplaceUserService.saveWorkplaceUser(1L, bean.getUid());

        // 3. dept
        deptUserService.saveDeptUser(request.getUserDept(), bean.getUid());

        // 4. group
        groupUserService.saveGroupUser(request.getUserGroup(), bean.getUid());

        // 5. position
        positionUserService.savePositionUser(request.getUserPosition(), bean.getUid());
    }

    @Transactional
    public void changeUser(Long uid, UserChange request) {
        if(!UserAuthorCd.isValidCode(request.getUserAuthor())) {
            throw new SimpleException("유효하지 않은 사용자 권한 코드입니다.");
        }

        // 0. metaData 묶기
        String processedMetaData = this.processMetaData(request.getMetaData());

        // 1. user
        CetusUser userView = dao.view(uid);
        dao.updateUserInfo(userView.changeUser(uid, request, processedMetaData));

        // 2. dept
        deptUserService.resetDeptUser(uid);
        deptUserService.saveDeptUser(request.getUserDept(), uid);

        // 3. group
        groupUserService.resetGroupUser(uid);
        groupUserService.saveGroupUser(request.getUserGroup(), uid);

        // 4. position
        positionUserService.resetPositionUser(uid);
        positionUserService.savePositionUser(request.getUserPosition(), uid);

        // 5. status
        if( request.getUserStatus() != null ) {
            dao.updateUserStatus(new CetusUser("STATUS", request.getUserStatus(), uid));
            statusHistService.saveUserStatusHistWithReason(uid, request.getUserStatus(), request.getChangeReason());
        }
    }

    // {metaData} 형태 만들기
    // => value 값이 obj 인 경우를 첨부파일 영역으로 우선 판단
    private String processMetaData(String jsonString) {
        Map<String, Object> map = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> parsedMap = objectMapper.readValue(jsonString, Map.class);
            for (Map.Entry<String, Object> entry : parsedMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Map) {
                    Map<?, ?> valueMap = (Map<?, ?>) value;
                    Long fileUid = null;
                    if (valueMap.get("fileUid") != null) {
                        fileUid = Long.valueOf(valueMap.get("fileUid").toString());
                    }
                    List<Map<String, Object>> fileAddRaw = (List<Map<String, Object>>) valueMap.get("fileAdd");
                    List<Map<String, Object>> fileDelRaw = (List<Map<String, Object>>) valueMap.get("fileDel");
                    List<CommonFile> fileAdd = fileAddRaw.stream()
                            .map(m -> CommonFile.castMapToCommonFile(m))
                            .collect(Collectors.toList());
                    List<CommonFile> fileDel = fileDelRaw.stream()
                            .map(m -> CommonFile.castMapToCommonFile(m))
                            .collect(Collectors.toList());

                    fileUid = commonFileService.processFileSeparately(fileAdd, fileDel, fileUid);

                    map.put(key, fileUid);
                } else {
                    map.put(key, value);
                }
            }
            String resultJson = objectMapper.writeValueAsString(map);
            return resultJson;
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    @Transactional
    public void changeMyInfo(Long uid, UserChangeMyInfo request) {
        // 0. metaData 묶기
        String processedMetaData = this.processMetaData(request.getMetaData());

        // 1. password
        String encodePassword = (request.getPassword() != null) ? passwordEncoder.encode(request.getPassword()) : null;

        // 1. user
        CetusUser userView = dao.view(uid);
        dao.updateUserMyInfo(userView.changeMyInfo(uid, request, processedMetaData, encodePassword));
    }

    @Transactional
    public void changeUserPassword(Long uid, UserPasswordChange request) {
        String encodePassword = passwordEncoder.encode(request.getPassword());
        CetusUser userView = dao.view(uid);
        dao.updateUserPassword(userView.changeUserPassword(uid, encodePassword));
    }

    @Async
    public void renderEXCEL(UserExcelSearch search) {
        search.setWorkplaceUid(UserUtil.getUserWorkplaceUid());
        try {
            excelCreate.createExcelFile(
                    UserExcelPage.class,
                    p -> dao.userExcelPage(search, p),
                    DownloadTargetCd.USER
            );
        } catch (Exception e) {
            log.error("엑셀 생성 실패", e);
        }
    }

    @Transactional(readOnly = true)
    public UserFullInfo findUserByUserId(String userId) {
        return dao.getUserByUserId(userId);
    }


    @Transactional(readOnly = true)
    public UserFullInfo findUserFullInfoByUserUid(Long uid) {
        return dao.getUserFullInfoByUserUid(uid);
    }

    @Transactional(readOnly = true)
    public UserView findUserInfoByUid(Long userUid) {
        return dao.getUserInfoByUid(userUid);
    }

    @Transactional(readOnly = true)
    public CetusUser view(Long uid) {
        return dao.view(uid);
    }

    @Transactional(readOnly = true)
    public CetusUser viewById(String userId) {
        return dao.viewById(userId);
    }

    @Transactional
    public void changeFailCnt(String userId) {
        dao.updateFailCnt(new CetusUser(userId));
    }

    @Transactional
    public void resetUserFailCnt(String userId) {
        dao.updateUserFailCnt(new CetusUser(userId));
    }

    @Transactional
    public void changeUseAt(String userId) {
        dao.updateUserUseAt(new CetusUser(userId));
    }

    @Transactional(readOnly = true)
    public Page<UserList> userPage(UserListSearch search, Pageable pageable) {
        search.setWorkplaceUid(UserUtil.getUserWorkplaceUid());
        return dao.userPage(search, pageable);
    }

    @Transactional
    public void userChangeInfo(UserChangeInfo request) {
        if("DEPT".equals(request.getCode())) {

            for(Long uid : request.getUsers()) {
                deptUserService.resetDeptUser(uid);
                deptUserService.saveDeptUser(request.getUid(), uid);
            }

        } else if( "STATUS".equals(request.getCode()) ) {

            for(Long uid : request.getUsers()) {
                if(!UserStatus.isValidCode(request.getValue())) {
                    throw new SimpleException("유효하지 않은 사용자 상태 코드입니다.");
                }
                dao.updateUserStatus(new CetusUser(request.getCode(), request.getValue(), uid));
                statusHistService.saveUserStatusHist(uid, request.getValue());
            }

        } else if( "AUTHOR".equals(request.getCode()) ) {

            for(Long uid : request.getUsers()) {
                if(!UserAuthorCd.isValidCode(request.getValue())) {
                    throw new SimpleException("유효하지 않은 사용자 권한 코드입니다.");
                }
                dao.updateUserAuthorCd(new CetusUser(request.getCode(), request.getValue(), uid));
            }

        }
    }

    @Transactional
    public void changeUserProfile(Long uid, UserProfile request) {
        Long profileUid = (request.getProfileUid() != null) ? request.getProfileUid() : null;
        profileUid = commonFileService.processFileBean(request, UserUtil.getUser(), profileUid);
        dao.updateUserProfile(new CetusUser(uid, profileUid));
    }

    /**
     * 이미 등록된 userEmail 있는지 체크
     * */
    public Integer findByEmail(String email) {
        return dao.findByEmail(email);
    }

    /**
     * 이미 등록된 userId 있는지 체크
     * */
    public Integer findByUserId(String userId) {
        return dao.findByUserId(userId);
    }
}
