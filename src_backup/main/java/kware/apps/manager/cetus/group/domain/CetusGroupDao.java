package kware.apps.manager.cetus.group.domain;


import cetus.dao.SuperDao;
import kware.apps.manager.cetus.group.dto.response.GroupList;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CetusGroupDao extends SuperDao<CetusGroup> {

    public CetusGroupDao() {
        super("cetusGroup");
    }


    public List<GroupList> getGroupList(Long workplaceUid) {
        return selectList("getGroupList", workplaceUid);
    }
}
