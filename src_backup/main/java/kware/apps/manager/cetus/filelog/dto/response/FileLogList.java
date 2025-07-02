package kware.apps.manager.cetus.filelog.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileLogList {

    private Long logUid;
    private Long fileUid;
    private String fileId;
    private String orgFileNm;
    private String extension;
    private String fileSize;
    private String downloadUrl;
    private String workerUid;
    private String workerNm;
    private String regDt;
}
