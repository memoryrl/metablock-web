package kware.apps.manager.cetus.workplace.workplaceuser.service;

import kware.apps.manager.cetus.workplace.workplaceuser.domain.CetusWorkplaceUser;
import kware.apps.manager.cetus.workplace.workplaceuser.domain.CetusWorkplaceUserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CetusWorkplaceUserService {

    private final CetusWorkplaceUserDao dao;

    @Transactional
    public void saveWorkplaceUser(Long workplaceUid, Long userUid){
        dao.insertWorkplaceUser(new CetusWorkplaceUser(workplaceUid, userUid));
    }

}
