package kware.apps.manager.cetus.answer.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerList {

    private Long answerUid;
    private Long bbscttUid;
    private String bbscttNm;
    private String answerCnt;
    private String regUid;
    private String regNm;
    private String regId;
    private String regDt;
}
