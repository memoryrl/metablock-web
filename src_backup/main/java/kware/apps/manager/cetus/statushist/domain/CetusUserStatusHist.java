package kware.apps.manager.cetus.statushist.domain;

import cetus.bean.AuditBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusUserStatusHist extends AuditBean {

    private Long uid;
    private Long userUid;
    private String reason;
    private String status;

    public CetusUserStatusHist(Long userUid, String status, String reason) {
        this.userUid = userUid;
        this.status = status;
        this.reason = reason;
    }
}
