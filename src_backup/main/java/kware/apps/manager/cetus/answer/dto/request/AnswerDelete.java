package kware.apps.manager.cetus.answer.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerDelete {

    private Long[] uids;
}
