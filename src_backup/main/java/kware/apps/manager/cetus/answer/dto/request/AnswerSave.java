package kware.apps.manager.cetus.answer.dto.request;


import cetus.annotation.DisplayName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerSave {

    @NotNull
    private Long bbscttUid;

    @NotBlank @DisplayName("댓글 내용")
    private String answerCnt;
}
