package kware.apps.manager.cetus.downloadshist.domain;

import cetus.bean.AuditBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusDownloadsHist extends AuditBean {

    private Long uid;
    private Long targetUid;
    private String targetCd;
    private Long fileUid;
    private Long userUid;

    public CetusDownloadsHist(String targetCd, Long targetUid, Long fileUid, Long userUid) {
        this.targetCd = targetCd;
        this.targetUid = targetUid;
        this.fileUid = fileUid;
        this.userUid = userUid;
    }

}
