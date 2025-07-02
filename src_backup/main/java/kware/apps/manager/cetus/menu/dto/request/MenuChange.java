package kware.apps.manager.cetus.menu.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuChange {

    private Long programUid;
    private String menuNm;
    private String menuIcon;
    private Integer sortNo;
    private String menuDc;
    private String useAt;
    private String menuStyle;
    private String menuStyle1;
    private String menuStyle2;

    public MenuChange(String menuNm, String useAt) {
        this.menuNm = menuNm;
        this.useAt = useAt;
    }

}
