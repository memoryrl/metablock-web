package kware.apps.manager.cetus.position.positionuser.domain;


import cetus.dao.SuperDao;
import kware.apps.manager.cetus.group.groupuser.domain.CetusGroupUser;
import org.springframework.stereotype.Component;

@Component
public class CetusPositionUserDao extends SuperDao<CetusPositionUser> {

    public CetusPositionUserDao() {
        super("cetusPositionUser");
    }

    public int insertPositionUser(CetusPositionUser bean) {
        return insert("insertPositionUser", bean);
    }

    public int deletePositionUser(Long userUid) {
        return delete("deletePositionUser", userUid);
    }
}
