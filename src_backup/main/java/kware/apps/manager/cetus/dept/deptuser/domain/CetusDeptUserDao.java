package kware.apps.manager.cetus.dept.deptuser.domain;

import cetus.dao.SuperDao;
import org.springframework.stereotype.Component;

@Component
public class CetusDeptUserDao extends SuperDao<CetusDeptUser> {

    public CetusDeptUserDao() {
        super("cetusDeptUser");
    }

    public int insertDeptUser(CetusDeptUser bean) {
        return insert("insertDeptUser", bean);
    }

    public int deleteDeptUser(Long userUid) {
        return delete("deleteDeptUser", userUid);
    }
}
