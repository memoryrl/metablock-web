package kware.apps.manager.cetus.group.groupuser.domain;

import cetus.dao.SuperDao;
import org.springframework.stereotype.Component;

@Component
public class CetusGroupUserDao extends SuperDao<CetusGroupUser> {

    public CetusGroupUserDao() {
        super("cetusGroupUser");
    }

    public int insertGroupUser(CetusGroupUser bean) {
        return insert("insertGroupUser", bean);
    }

    public int deleteGroupUser(Long userUid) {
        return delete("deleteGroupUser", userUid);
    }
}
