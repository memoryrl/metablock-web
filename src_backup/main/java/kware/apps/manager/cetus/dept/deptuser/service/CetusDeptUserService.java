package kware.apps.manager.cetus.dept.deptuser.service;


import kware.apps.manager.cetus.dept.deptuser.domain.CetusDeptUser;
import kware.apps.manager.cetus.dept.deptuser.domain.CetusDeptUserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CetusDeptUserService {

    private final CetusDeptUserDao dao;

    @Transactional
    public void saveDeptUser(Long deptUid, Long userUid) {
        dao.insertDeptUser(new CetusDeptUser(deptUid, userUid));
    }

    @Transactional
    public void resetDeptUser(Long userUid) {
        dao.deleteDeptUser(userUid);
    }

}
