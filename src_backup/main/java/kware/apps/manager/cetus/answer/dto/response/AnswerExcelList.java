package kware.apps.manager.cetus.answer.dto.response;

import cetus.annotation.ExcelColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerExcelList {

    @ExcelColumn(headerName = "게시판명")
    private String bbsNm;
    @ExcelColumn(headerName = "게시판 분류")
    private String bbsTpCdNm;
    @ExcelColumn(headerName = "게시글 제목")
    private String bbscttNm;
    @ExcelColumn(headerName = "댓글내용")
    private String answerCnt;
    @ExcelColumn(headerName = "작성일")
    private String regDt;
    @ExcelColumn(headerName = "작성자")
    private String regInfo;
    @ExcelColumn(headerName = "게시글URL")
    private String bbscttUrl;

    public void setAnswerCnt(String answerCnt) {
        this.answerCnt = answerCnt;
    }
}
