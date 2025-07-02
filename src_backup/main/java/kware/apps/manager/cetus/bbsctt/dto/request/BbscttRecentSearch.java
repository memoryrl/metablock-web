package kware.apps.manager.cetus.bbsctt.dto.request;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BbscttRecentSearch {

    private Long workplaceUid;
    private int count;

    public BbscttRecentSearch(Long workplaceUid, int count) {
        this.workplaceUid = workplaceUid;
        this.count = count;
    }
}
