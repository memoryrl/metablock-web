package kware.apps.manager.cetus.user.dto.request;

import cetus.annotation.ValidPassword;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPasswordChange {

    @ValidPassword
    private String password;
}
