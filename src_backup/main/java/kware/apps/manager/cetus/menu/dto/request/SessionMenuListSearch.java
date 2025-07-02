package kware.apps.manager.cetus.menu.dto.request;

import lombok.Getter;


@Getter
public class SessionMenuListSearch {

    private String useAt;
    private String authorCd;
    private Long workplaceUid;
    private Long menuNo;        // 부모 root menu no

    public SessionMenuListSearch(String useAt, String authorCd, Long workplaceUid) {
        this.useAt = useAt;
        this.authorCd = authorCd;
        this.workplaceUid = workplaceUid;
    }

    public SessionMenuListSearch(String useAt, String authorCd, Long workplaceUid, Long menuNo) {
        this.useAt = useAt;
        this.authorCd = authorCd;
        this.workplaceUid = workplaceUid;
        this.menuNo = menuNo;
    }
}
