package kware.apps.manager.cetus.downloadshist.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DownloadsHistList {

    private Long uid;
    private Long targetUid;
    private String targetCd;
    private String targetNm;
    private Long userUid;
    private Long fileUid;
    private String fileId;
    private String orgFileNm;
    private String fileUrl;
    private int downCnt;
}
