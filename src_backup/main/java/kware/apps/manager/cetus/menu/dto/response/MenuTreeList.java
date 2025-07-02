package kware.apps.manager.cetus.menu.dto.response;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuTreeList {

    private Long no;
    private Long upperNo;
    private String text;
    private Long programUid;
    private String treeMenuNm;
    private String menuIcon;
    private Integer sortNo;
    private String menuDc;
    private String useAt;
    private Long regUid;
    private String regDt;
    private String regNm;
    private Long updtUid;
    private String updtDt;
    private String updtNm;
    private String authorCd;
    private String menuStyle;
    private String menuStyle1;
    private String menuStyle2;
    private String rootMenuCd;
    private String rootMenuCdNm;
    private Long workplaceUid;
    private String url;
    private Integer depth;
    private String menuPath;
    private String isLeaf;

    public Integer getSortNoForTreeMenu() {
        return sortNo != null ? sortNo : -1;
    }
}
