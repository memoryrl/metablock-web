package kware.apps.manager.cetus.user.dto.request;

import cetus.annotation.DisplayName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED) @ToString
public class UserChange {

    @NotBlank @DisplayName("사용자 이름")
    private String userNm;

    @NotBlank @DisplayName("사용자 이메일")
    private String userEmail;

    @NotNull @DisplayName("사용자 소속")
    private Long userGroup;

    @NotNull @DisplayName("사용자 그룹/부서")
    private Long userDept;

    @NotNull @DisplayName("사용자 직급")
    private Long userPosition;

    @NotBlank @DisplayName("사용자 권한")
    private String userAuthor;

    private String userStatus;
    private String changeReason;
    private String metaData;
}
