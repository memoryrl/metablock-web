package kware.apps.manager.cetus.group.groupuser.service;


import kware.apps.manager.cetus.group.groupuser.domain.CetusGroupUser;
import kware.apps.manager.cetus.group.groupuser.domain.CetusGroupUserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CetusGroupUserService {

    private final CetusGroupUserDao dao;

    @Transactional
    public void saveGroupUser(Long groupUid, Long userUid) {
        dao.insertGroupUser(new CetusGroupUser(groupUid, userUid));
    }

    @Transactional
    public void resetGroupUser(Long userUid) {
        dao.deleteGroupUser(userUid);
    }
}
