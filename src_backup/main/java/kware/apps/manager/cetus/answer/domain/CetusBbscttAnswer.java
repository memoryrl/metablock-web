package kware.apps.manager.cetus.answer.domain;

import cetus.bean.AuditBean;
import kware.apps.manager.cetus.answer.dto.request.AnswerChange;
import kware.apps.manager.cetus.answer.dto.request.AnswerSave;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusBbscttAnswer extends AuditBean {

    private Long answerUid;
    private Long bbscttUid;
    private String answerCnt;
    private String deleteAt;

    public CetusBbscttAnswer(AnswerSave request) {
        this.bbscttUid = request.getBbscttUid();
        this.answerCnt = request.getAnswerCnt();
    }

    public CetusBbscttAnswer(Long answerUid, AnswerChange request) {
        this.answerUid = answerUid;
        this.answerCnt = request.getAnswerCnt();
    }
}
