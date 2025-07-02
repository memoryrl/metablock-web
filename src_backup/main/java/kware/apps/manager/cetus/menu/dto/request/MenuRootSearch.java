package kware.apps.manager.cetus.menu.dto.request;

import lombok.Getter;

@Getter
public class MenuRootSearch {

    private String authorCd;
    private String rootMenuCd;
    private Long workplaceUid;

    public MenuRootSearch(String authorCd, String rootMenuCd, Long workplaceUid) {
        this.authorCd = authorCd;
        this.rootMenuCd = rootMenuCd;
        this.workplaceUid = workplaceUid;
    }
}
