package kware.apps.manager.cetus.workplace.workplaceuser.domain;

import cetus.bean.AuditBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusWorkplaceUser extends AuditBean {

    private Long workplaceUid;
    private Long userUid;

    public CetusWorkplaceUser(Long workplaceUid, Long userUid) {
        this.workplaceUid = workplaceUid;
        this.userUid = userUid;
    }
}
