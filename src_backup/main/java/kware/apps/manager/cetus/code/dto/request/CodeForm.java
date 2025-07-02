package kware.apps.manager.cetus.code.dto.request;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeForm {
    @NotBlank
    private String code;
    @Valid
    private CodeParentForm parent;

    private List<CodeChildForm> addList;
    private List<CodeChildForm> updtList;
}
