package kware.apps.manager.cetus.bbsctt.dto.response;


import cetus.annotation.ExcelColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BbscttExcelList {

    @ExcelColumn(headerName = "게시판명")
    private String bbsNm;
    @ExcelColumn(headerName = "게시판 분류")
    private String bbsTpCdNm;
    @ExcelColumn(headerName = "게시글 제목")
    private String bbscttNm;
    @ExcelColumn(headerName = "조회수")
    private int rdCnt;
    @ExcelColumn(headerName = "공지 여부")
    private String noticeAt;
    @ExcelColumn(headerName = "첨부파일 개수")
    private int fileCnt;
    @ExcelColumn(headerName = "공개 여부")
    private String openAt;
    @ExcelColumn(headerName = "작성일")
    private String regDt;
    @ExcelColumn(headerName = "작성자")
    private String regInfo;
    @ExcelColumn(headerName = "게시글URL")
    private String bbscttUrl;
}
