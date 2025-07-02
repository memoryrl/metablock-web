package kware.apps.manager.cetus.menu.dto.response;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionMenuList {

    private Long menuNo;
    private Long programUid;
    private Long upperMenuNo;
    private String menuNm;
    private String treeMenuNm;
    private String menuIcon;
    private Integer sortNo;
    private String menuDc;
    private String useAt;
    private String deleteAt;

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
    private Long workplaceUid;

    private String leftSlideImgId;
    private String rightSlideImgId;
    private String logoImgId;
    private String companyLogoImgId;
    private String title1;
    private String title2;
    private String title3;

    private String url;
    private Integer depth;
    private String menuPath;
    private String isLeaf;

    public String getUrl() {
        return StringUtils.hasText(url) ? url : "";
    }

    public Integer getSortNoForTreeMenu() {
        return sortNo != null ? sortNo : -1;
    }

}
