package kware.apps.manager.cetus.user.dto.request;

import cetus.annotation.DisplayName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSaveAdmin {

    @NotBlank
    @DisplayName("사용자ID")
    private String userId;

    @NotBlank @Pattern(regexp = "^(?!$).{8,20}$") @DisplayName("비밀번호")
    private String password;

    @NotBlank @DisplayName("사용자 이름")
    private String userNm;

    @NotBlank @DisplayName("사용자 이메일")
    private String userEmail;

    @NotBlank @DisplayName("권한")
    private String authorCd;

    @NotNull
    @DisplayName("그룹/부서")
    private Long userDept;

    @NotNull @DisplayName("소속")
    private Long userGroup;

    @NotNull @DisplayName("직급")
    private Long userPosition;

    private String status;
    private String metaData;
}
