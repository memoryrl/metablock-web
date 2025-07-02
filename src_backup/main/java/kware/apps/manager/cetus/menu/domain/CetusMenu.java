package kware.apps.manager.cetus.menu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kware.apps.manager.cetus.menu.dto.response.SessionMenuList;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CetusMenu implements Serializable {
    @JsonIgnore
    private final Long menuNo;
    private final String menuNm;
    private final String url;
    private final int sortNo;
    private final String menuIcon;
    private final String menuStyle;
    private final String menuStyle1;
    private final String menuStyle2;
    private final String leftSlideImgId;
    private final String rightSlideImgId;
    private final List<CetusMenu> children = new ArrayList<>();

    public CetusMenu(SessionMenuList menuInfo) {

        this.menuNo = menuInfo.getMenuNo();
        this.menuNm = menuInfo.getMenuNm();
        this.menuIcon = menuInfo.getMenuIcon();
        this.menuStyle = menuInfo.getMenuStyle();
        this.menuStyle1 = menuInfo.getMenuStyle1();
        this.menuStyle2 = menuInfo.getMenuStyle2();
        this.leftSlideImgId = menuInfo.getLeftSlideImgId();
        this.rightSlideImgId = menuInfo.getRightSlideImgId();
        if (menuInfo.getUrl() != null) {
            this.url = menuInfo.getUrl().equals("/") ? "#" : menuInfo.getUrl();
        } else {
            this.url = "#";
        }

        this.sortNo = menuInfo.getSortNo();
    }

    public boolean hasChild() {
        return !children.isEmpty();
    }
}
