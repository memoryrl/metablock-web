package kware.apps.manager.cetus.bbs.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BbsList {

    private Long bbsUid;
    private String bbsNm;
    private String bbsTpCd;
    private String bbsTpCdNm;
    private String useAt;
    private int bbscttCnt;
    private Long regUid;
    private String regDt;
    private String regNm;
    private String deleteAt;
    private String workplaceNm;
    private Long workplaceUid;
    private String bbsClUseAt;
    private String atchAt;
    private Integer atchNum;
    private Integer uploadCpcty;
    private String answerUseAt;
}
