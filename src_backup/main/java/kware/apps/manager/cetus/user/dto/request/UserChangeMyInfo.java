package kware.apps.manager.cetus.user.dto.request;


import cetus.annotation.DisplayName;
import cetus.annotation.ValidPassword;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChangeMyInfo {

    @NotBlank @DisplayName("사용자 이름")
    private String userNm;

    @NotBlank @DisplayName("사용자 이메일")
    private String userEmail;

    private String metaData;

    @ValidPassword(allowNull = true)
    private String password;
}
