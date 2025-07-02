package kware.apps.asp.contents.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentsPage {

    private Long commentsUid;
    private Long contentsUid;
    private String type;
    private String typeStr;
    private String ratings;
    private String comment;
    private Long regUid;
    private String regDt;
    private String regNm;
    private String regProfileId;

}
