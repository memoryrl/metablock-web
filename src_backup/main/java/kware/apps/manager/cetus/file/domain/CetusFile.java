package kware.apps.manager.cetus.file.domain;

import cetus.bean.AuditBean;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusFile extends AuditBean {

    private Long fileUid;
    private String fileId;
    private String fileNm;
    private String orgFileNm;
    private String filePath;
    private String fileUrl;
    private Long fileSize;
    private String fileType;
    private String extension;
    private int downCnt;
    private String useAt;
    private String saved;
    private String regId;

    public CetusFile(Long fileUid, String fileId, String orgFileNm, String regId) {
        this.fileUid = fileUid;
        this.fileId = fileId;
        this.orgFileNm = orgFileNm;
        this.regId = regId;
    }

    @Builder(builderMethodName = "updateDownloadFile")
    public CetusFile(Long fileUid, String fileId, String fileNm,
                     String filePath, String fileUrl, Long fileSize,
                     String extension){
        this.fileUid = fileUid;
        this.fileId = fileId;
        this.fileNm = fileNm;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.extension = extension;
        this.fileUrl = fileUrl;
    }
}
