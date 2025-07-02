package kware.common.file.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommonFileLog {

    private Long fileUid;
    private String fileId;
    private Long logUid;

    private String workerUid;
    private String workerNm;

    private Integer downCnt;

    private String regDt;
    private String downloadUrl;

}
