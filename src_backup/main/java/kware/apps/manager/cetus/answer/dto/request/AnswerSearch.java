package kware.apps.manager.cetus.answer.dto.request;

import lombok.Getter;

@Getter
public class AnswerSearch {

    private Long bbsUid;
    private Long regUid;

    public AnswerSearch(Long bbsUid, Long regUid) {
        this.bbsUid = bbsUid;
        this.regUid = regUid;
    }
}
