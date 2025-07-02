package kware.apps.manager.cetus.loginhist.dto.response;


import cetus.annotation.ExcelColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginHistExcelList {

    @ExcelColumn(headerName = "사용자 정보")
    private String userInfo;
    @ExcelColumn(headerName = "로그인 일시")
    private String loginDt;
    @ExcelColumn(headerName = "로그인 IP")
    private String loginIp;
    @ExcelColumn(headerName = "접속 지역")
    private String loginRegion;
    @ExcelColumn(headerName = "접속 브라우저")
    private String loginBrowser;
    @ExcelColumn(headerName = "접속 URL")
    private String loginAccessUrl;
    @ExcelColumn(headerName = "세션 ID")
    private String sessionId;

}
