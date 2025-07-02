package kware.apps.manager.cetus.position.positionuser.domain;

import cetus.bean.AuditBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusPositionUser extends AuditBean {

    private Long positionUid;
    private Long userUid;

    public CetusPositionUser(Long positionUid, Long userUid) {
        this.positionUid = positionUid;
        this.userUid = userUid;
    }
}
