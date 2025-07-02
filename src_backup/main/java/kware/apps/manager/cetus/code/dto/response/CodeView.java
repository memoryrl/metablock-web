package kware.apps.manager.cetus.code.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeView {

    private String lv;
    private Long uid;
    private String code;
    private String upperCode;
    private String codeNm;
    private String codeDc;
    private String useAt;
    private String rmDc;
    private String item1Val;
    private String item2Val;
    private Integer sortNo;
}
