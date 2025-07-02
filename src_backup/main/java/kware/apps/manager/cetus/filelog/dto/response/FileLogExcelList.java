package kware.apps.manager.cetus.filelog.dto.response;

import cetus.annotation.ExcelColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileLogExcelList {

    @ExcelColumn(headerName = "파일 ID")
    private String fileId;
    @ExcelColumn(headerName = "파일 이름")
    private String orgFileNm;
    @ExcelColumn(headerName = "파일 확장자")
    private String extension;
    @ExcelColumn(headerName = "파일 크기")
    private String fileSize;
    @ExcelColumn(headerName = "파일 다운로드 URL")
    private String downloadUrl;
    @ExcelColumn(headerName = "사용자 이름")
    private String workerNm;
    @ExcelColumn(headerName = "다운로드 일시")
    private String regDt;
}
