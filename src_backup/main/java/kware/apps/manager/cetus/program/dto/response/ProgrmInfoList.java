package kware.apps.manager.cetus.program.dto.response;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgrmInfoList {

    private Long uid;
    private String progrmNm;
    private String url;
    private String progrmDc;
    private String useAt;
    private Long regUid;
    private String regDt;
    private String regNm;

}
