package kware.apps.manager.cetus.statushist.dto.response;


import cetus.annotation.ExcelColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatusHistExcelList {

    @ExcelColumn(headerName = "사용자")
    private String userInfo;
    @ExcelColumn(headerName = "워크플레이스 이름")
    private String workplaceNm;
    @ExcelColumn(headerName = "변경 사유")
    private String reason;
    @ExcelColumn(headerName = "변경 후 상태")
    private String status;
    @ExcelColumn(headerName = "변경 후 상태명")
    private String statusNm;
    @ExcelColumn(headerName = "변경 일시")
    private String regDt;
    @ExcelColumn(headerName = "변경자")
    private String regInfo;
}
