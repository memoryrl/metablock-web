package kware.apps.manager.cetus.bbs.dto.request;

import cetus.annotation.DisplayName;
import cetus.annotation.YOrN;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BbsChange {

    @NotBlank @DisplayName("게시판 이름")
    private String bbsNm;

    @YOrN
    private String useAt;

    @YOrN
    private String bbsClUseAt;

    @YOrN
    private String atchAt;

    private Integer atchNum;

    private Integer uploadCpcty;

    @YOrN
    private String answerUseAt;
}
