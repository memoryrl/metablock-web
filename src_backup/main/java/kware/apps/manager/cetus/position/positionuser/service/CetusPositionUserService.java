package kware.apps.manager.cetus.position.positionuser.service;


import kware.apps.manager.cetus.group.groupuser.domain.CetusGroupUser;
import kware.apps.manager.cetus.position.positionuser.domain.CetusPositionUser;
import kware.apps.manager.cetus.position.positionuser.domain.CetusPositionUserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CetusPositionUserService {

    private final CetusPositionUserDao dao;

    @Transactional
    public void savePositionUser(Long positionUid, Long userUid) {
        dao.insertPositionUser(new CetusPositionUser(positionUid, userUid));
    }

    @Transactional
    public void resetPositionUser(Long userUid) {
        dao.deletePositionUser(userUid);
    }
}
