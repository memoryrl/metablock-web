package kware.apps.manager.cetus.menu.dto.request;

import lombok.Getter;


@Getter
public class MenuTreeSearch {

    private String authorCd;
    private Long workplaceUid;

    public MenuTreeSearch(String authorCd, Long workplaceUid) {
        this.authorCd = authorCd;
        this.workplaceUid = workplaceUid;
    }

    public void setWorkplaceUid(Long workplaceUid) {
        this.workplaceUid = workplaceUid;
    }
}
