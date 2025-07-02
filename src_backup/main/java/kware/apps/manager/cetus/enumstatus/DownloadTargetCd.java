package kware.apps.manager.cetus.enumstatus;


import lombok.Getter;

@Getter
public enum DownloadTargetCd {

    CONTENTS("컨텐츠", "컨텐츠_다운로드_"),
    USER("유저", "유저목록_다운로드_"),
    LOGIN_HIST("접속 기록", "유저_로그인기록_다운로드_"),
    DOWNLOAD_HIST("파일 다운로드 기록", "유저_파일다운로드_기록_"),
    USER_STATUS_HIST("유저 상태 변경 기록", "유저_상태변경이력_다운로드_"),
    USER_BBSCTT("유저가 작성한 게시글", "유저_작성게시글_다운로드_"),
    USER_ANSWER("유저가 작성한 댓글", "유저_작성댓글_다운로드_");

    private String description;
    private String fileHeaderNm;

    DownloadTargetCd(String description, String fileHeaderNm) {
        this.description = description;
        this.fileHeaderNm = fileHeaderNm;
    }
}
