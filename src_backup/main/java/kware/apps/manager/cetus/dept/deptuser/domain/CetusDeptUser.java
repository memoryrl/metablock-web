package kware.apps.manager.cetus.dept.deptuser.domain;

import cetus.bean.AuditBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusDeptUser extends AuditBean {

    private Long deptUid;
    private Long userUid;

    public CetusDeptUser(Long deptUid, Long userUid) {
        this.deptUid = deptUid;
        this.userUid = userUid;
    }
}
