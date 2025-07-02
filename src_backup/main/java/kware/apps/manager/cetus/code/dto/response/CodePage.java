package kware.apps.manager.cetus.code.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodePage {
    private Long uid;
    private String code;
    private String upperCode;
    private String codeNm;
    private String codeDc;
    private String useAt;
    private Long regUid;
    private String regDt;
    private Long updtUid;
    private String updtDt;
    private String regNm;
    private Long checkChild;
}
