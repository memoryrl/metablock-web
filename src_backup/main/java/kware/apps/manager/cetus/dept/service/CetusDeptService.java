package kware.apps.manager.cetus.dept.service;

import cetus.user.UserUtil;
import kware.apps.manager.cetus.dept.domain.CetusDeptDao;
import kware.apps.manager.cetus.dept.dto.response.DeptTreeList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CetusDeptService {

    private final CetusDeptDao dao;

    @Transactional(readOnly = true)
    public List<DeptTreeList> findDeptTreeList() {
        return dao.findDeptTreeList(UserUtil.getUser().getWorkplaceUid());
    }
}
