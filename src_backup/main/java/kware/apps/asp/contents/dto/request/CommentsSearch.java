package kware.apps.asp.contents.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CommentsSearch {

    private Long contentsUid;
    private Long workplaceUid;
    private String type;

    public CommentsSearch(Long workplaceUid, String type) {
        this.workplaceUid = workplaceUid;
        this.type = type;
    }

    public CommentsSearch(Long workplaceUid, Long contentsUid) {
        this.workplaceUid = workplaceUid;
        this.contentsUid = contentsUid;
    }
}