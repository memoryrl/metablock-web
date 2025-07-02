package kware.apps.manager.cetus.user.dto.response;

import cetus.annotation.ExcelColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserExcelPage {

    @ExcelColumn(headerName = "사용자 ID")
    private String userId;
    @ExcelColumn(headerName = "사용자 이름")
    private String userNm;
    @ExcelColumn(headerName = "사용자 이메일")
    private String userEmail;
    @ExcelColumn(headerName = "사용자 권한")
    private String roleNm;
    @ExcelColumn(headerName = "사용자 상태")
    private String statusNm;
    @ExcelColumn(headerName = "사용자 사용 여부")
    private String useAt;
    @ExcelColumn(headerName = "사용자 승인 여부")
    private String approveAt;
    @ExcelColumn(headerName = "사용자 워크플레이스 명")
    private String workplaceNm;
    @ExcelColumn(headerName = "사용자 소속 명")
    private String groupNm;
    @ExcelColumn(headerName = "사용자 그룹/부서 명")
    private String deptNm;
    @ExcelColumn(headerName = "사용자 직급 명")
    private String positionNm;
    @ExcelColumn(headerName = "사용자 생성일시")
    private String regDt;
}
