package kware.apps.manager.cetus.code.dto.request;

import cetus.annotation.DisplayName;
import cetus.annotation.YOrN;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeSave {
    @NotBlank @DisplayName("코드ID")
    private String code;

    private String upperCode;

    @NotBlank @DisplayName("코드명")
    private String codeNm;

    private String codeDc;

    @YOrN(allowNull = false) @DisplayName("사용여부")
    private String useAt;

    private String rmDc;
    private String item1Val;
    private String item2Val;
    private Integer sortNo;
}
