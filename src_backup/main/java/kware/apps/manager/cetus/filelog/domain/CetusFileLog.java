package kware.apps.manager.cetus.filelog.domain;

import cetus.bean.AuditBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusFileLog extends AuditBean {

    private Long logUid;
    private Long fileUid;
    private String fileId;
    private String workerUid;
    private String workerNm;
    private String regDt;
    private String downloadUrl;

    public CetusFileLog(Long fileUid, String fileId, String workerUid, String workerNm, String downloadUrl) {
        this.fileUid = fileUid;
        this.fileId = fileId;
        this.workerUid = workerUid;
        this.workerNm = workerNm;
        this.downloadUrl = downloadUrl;
    }

}
