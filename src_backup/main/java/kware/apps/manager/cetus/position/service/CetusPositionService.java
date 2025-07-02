package kware.apps.manager.cetus.position.service;


import cetus.user.UserUtil;
import kware.apps.manager.cetus.position.domain.CetusPositionDao;
import kware.apps.manager.cetus.position.dto.response.PositionList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CetusPositionService {

    private final CetusPositionDao dao;

    @Transactional(readOnly = true)
    public List<PositionList> findPositionList() {
        return dao.getPositionList(UserUtil.getUser().getWorkplaceUid());
    }
}
