package kware.apps.manager.cetus.statushist.service;


import cetus.bean.Page;
import cetus.bean.Pageable;
import kware.apps.manager.cetus.enumstatus.DownloadTargetCd;
import kware.apps.manager.cetus.statushist.domain.CetusUserStatusHist;
import kware.apps.manager.cetus.statushist.domain.CetusUserStatusHistDao;
import kware.apps.manager.cetus.statushist.dto.request.UserStatusHistExcelSearch;
import kware.apps.manager.cetus.statushist.dto.request.UserStatusHistSearch;
import kware.apps.manager.cetus.statushist.dto.response.UserStatusHistExcelList;
import kware.apps.manager.cetus.statushist.dto.response.UserStatusHistList;
import kware.common.excel.ExcelCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class CetusUserStatusHistService {

    private final CetusUserStatusHistDao dao;
    private final ExcelCreate excelCreate;

    private final String reason = "사용자 관리 목록 화면에서 관리자의 사용자 상태 변경";

    @Transactional
    public void saveUserStatusHist(Long userUid, String status) {
        dao.insertUserStatusHist(new CetusUserStatusHist(userUid, status, reason));
    }

    @Transactional
    public void saveUserStatusHistWithReason(Long userUid, String status, String changeReason) {
        dao.insertUserStatusHist(new CetusUserStatusHist(userUid, status, changeReason));
    }

    @Transactional(readOnly = true)
    public Page<UserStatusHistList> findAllUserStatusHistPage(UserStatusHistSearch search, Pageable pageable) {
        return dao.page("userStatusHistList", "userStatusHistCount", search, pageable);
    }

    @Async
    public void renderEXCEL(UserStatusHistExcelSearch search) {
        excelCreate.createExcelFile(
                UserStatusHistExcelList.class,
                p -> dao.userStatusHistExcelPage(search, p),
                DownloadTargetCd.USER_STATUS_HIST
        );
    }
}
