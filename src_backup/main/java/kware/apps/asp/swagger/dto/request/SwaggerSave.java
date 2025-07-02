// package kware.apps.asp.swagger.dto.request;

// import cetus.annotation.DisplayName;
// import io.swagger.annotations.ApiModel;
// import io.swagger.annotations.ApiModelProperty;
// import lombok.AccessLevel;
// import lombok.Getter;
// import lombok.NoArgsConstructor;

// import javax.validation.constraints.NotBlank;

// @ApiModel(description = "Swagger 저장 테스트 DTO")
// @Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
// public class SwaggerSave {

//     @NotBlank
//     @DisplayName("사용자ID")
//     @ApiModelProperty(value = "사용자 ID", example = "user01", required = true)
//     private String userId;

//     @NotBlank @DisplayName("비밀번호")
//     @ApiModelProperty(value = "사용자 비밀번호", example = "1234", required = true)
//     private String password;

//     @NotBlank @DisplayName("사용자 이름")
//     @ApiModelProperty(value = "사용자 이름", example = "테스트유저", required = true)
//     private String userNm;

//     @NotBlank @DisplayName("사용자 전화번호")
//     @ApiModelProperty(value = "사용자 전화번호", example = "010-1234-5678", required = true)
//     private String userTel;

//     @NotBlank @DisplayName("사용자 이메일")
//     @ApiModelProperty(value = "사용자 이메일", example = "user01@naver.com", required = true)
//     private String userEmail;

// }
