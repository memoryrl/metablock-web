package kware.apps.manager.cetus.user.domain;

import cetus.bean.Page;
import cetus.bean.Pageable;
import cetus.dao.SuperDao;
import kware.apps.manager.cetus.user.dto.request.UserExcelSearch;
import kware.apps.manager.cetus.user.dto.request.UserListSearch;
import kware.apps.manager.cetus.user.dto.response.UserExcelPage;
import kware.apps.manager.cetus.user.dto.response.UserFullInfo;
import kware.apps.manager.cetus.user.dto.response.UserList;
import kware.apps.manager.cetus.user.dto.response.UserView;
import org.springframework.stereotype.Component;


@Component
public class CetusUserDao extends SuperDao<CetusUser> {

    public CetusUserDao() {
        super("cetusUser");
    }

    public int updateUserInfo(CetusUser bean) {
        return update("updateUserInfo", bean);
    }

    public int updateUserMyInfo(CetusUser bean) {
        return update("updateUserMyInfo", bean);
    }

    public int updateUserPassword(CetusUser bean) {
        return update("updateUserPassword", bean);
    }

    public UserFullInfo getUserByUserId(String userId) {
        return selectOne("getUserByUserId", userId);
    }

    public UserFullInfo getUserFullInfoByUserUid(Long uid) {
        return selectOne("getUserFullInfoByUserUid", uid);
    }

    public int updateFailCnt(CetusUser bean) {
        return update("updateFailCnt", bean);
    }

    public int updateUserFailCnt(CetusUser bean) {
        return update("updateUserFailCnt", bean);
    }

    public int updateUserUseAt(CetusUser bean) {
        return update("updateUserUseAt", bean);
    }

    public UserView getUserInfoByUid(Long userUid) {
        return selectOne("getUserInfoByUid", userUid);
    }

    public Page<UserList> userPage(UserListSearch search, Pageable pageable) {
        return page("userPageList", "userPageListCount", search, pageable);
    }

    public Page<UserExcelPage> userExcelPage(UserExcelSearch search, Pageable pageable) {
        return page("userExcelPageList", "userExcelPageCount", search, pageable);
    }

    public int updateUserStatus(CetusUser bean) {
        return update("updateUserStatus", bean);
    }

    public int updateUserAuthorCd(CetusUser bean) {
        return update("updateUserAuthorCd", bean);
    }

    public int updateUserProfile(CetusUser bean) {
        return update("updateUserProfile", bean);
    }

    public Integer findByUserId(String userId) {
        return selectOne("findByUserId", userId);
    }

    public Integer findByEmail(String email) {
        return selectOne("findByEmail", email);
    }

    public CetusUser viewById(String userId) {
        return selectOne("viewById", userId);
    }
}
