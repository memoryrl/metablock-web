package kware.apps.manager.cetus.group.groupuser.domain;

import cetus.bean.AuditBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusGroupUser extends AuditBean {

    private Long groupUid;
    private Long userUid;

    public CetusGroupUser(Long groupUid, Long userUid) {
        this.groupUid = groupUid;
        this.userUid = userUid;
    }
}
