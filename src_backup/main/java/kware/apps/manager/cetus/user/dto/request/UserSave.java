package kware.apps.manager.cetus.user.dto.request;

import cetus.annotation.DisplayName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSave {

    @NotBlank @DisplayName("사용자ID")
    private String userId;

    @NotBlank @Pattern(regexp = "^(?!$).{8,20}$") @DisplayName("비밀번호")
    private String password;

    @NotBlank @DisplayName("사용자 이름")
    private String userNm;

    @NotBlank @DisplayName("사용자 이메일")
    private String userEmail;

    private String authorCd;

    private String status;

    private String metaData;

//    private Long userDept;
//    private Long userGroup;
//    private Long userPosition;
}
