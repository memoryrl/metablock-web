package kware.apps.manager.cetus.bbsctt.dto.request;

import cetus.annotation.DisplayName;
import cetus.annotation.YOrN;
import cetus.bean.FileBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BbscttChange extends FileBean {

    private Long fileUid;

    @NotBlank @DisplayName("게시글 제목")
    private String bbscttNm;

    @NotBlank @DisplayName("게시글 내용")
    private String bbscttCnt;

    @YOrN @DisplayName("공개 여부")
    private String openAt;

    @YOrN(allowNull = true) @DisplayName("공지 여부")
    private String noticeAt;

    private String thumbnailSrc;
}
