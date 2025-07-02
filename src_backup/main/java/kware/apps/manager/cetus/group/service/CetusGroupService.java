package kware.apps.manager.cetus.group.service;


import cetus.user.UserUtil;
import kware.apps.manager.cetus.group.domain.CetusGroupDao;
import kware.apps.manager.cetus.group.dto.response.GroupList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CetusGroupService {

    private final CetusGroupDao dao;

    @Transactional(readOnly = true)
    public List<GroupList> findGroupList() {
        return dao.getGroupList(UserUtil.getUser().getWorkplaceUid());
    }
}
