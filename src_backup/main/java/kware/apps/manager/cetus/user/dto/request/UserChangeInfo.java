package kware.apps.manager.cetus.user.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChangeInfo {

    @NotBlank
    private String code;        // [DEPT, STATUS, AUTHOR]

    private Long uid;           // if dept

    private String value;       // if status, author

    private Long[] users;

}
