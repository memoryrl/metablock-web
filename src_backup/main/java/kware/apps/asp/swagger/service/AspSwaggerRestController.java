// package kware.apps.asp.swagger.service;


// import io.swagger.annotations.Api;
// import io.swagger.annotations.ApiParam;
// import io.swagger.v3.oas.annotations.Operation;
// import kware.apps.asp.swagger.dto.request.SwaggerSave;
// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import javax.validation.Valid;

// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/asp/api")
// @Api(tags = {"01. UST21 API"}, description = "UST21 API")
// public class AspSwaggerRestController {

//     @PostMapping
//     @Operation(description = "UST21 저장 테스트 API", summary = "UST21 저장 테스트 API 입니다.")
//     public ResponseEntity saveUser(@RequestBody @Valid SwaggerSave request) {
//         return ResponseEntity.ok("저장됐습니다.");
//     }


//     @PutMapping("/{uid}")
//     @Operation(description = "UST21 수정 테스트 API", summary = "UST21 수정 테스트 API 입니다.")
//     public ResponseEntity changeUser(
//             @ApiParam(value = "사용자 UID", required = true) @PathVariable("uid") Long uid,
//             @RequestBody @Valid SwaggerSave request) {
//         return ResponseEntity.ok("저장됐습니다.");
//     }

//     @GetMapping("/{uid}")
//     @Operation(description = "UST21 단건 조회 테스트 API", summary = "UST21 단건 조회 테스트 API")
//     public ResponseEntity findByUserUid(@ApiParam(value = "사용자 UID", required = true) @PathVariable("uid") Long uid) {
//         return ResponseEntity.ok("사용자 단건 조회 정보입니다.");
//     }

//     @DeleteMapping("/{uid}")
//     @Operation(description = "UST21 단건 삭제 테스트 API", summary = "UST21 단건 삭제 테스트 API")
//     public ResponseEntity deleteUser(@ApiParam(value = "사용자 UID", required = true) @PathVariable("uid") Long uid) {
//         return ResponseEntity.ok("사용자 단건 정보가 삭제되었습니다.");
//     }
// }
