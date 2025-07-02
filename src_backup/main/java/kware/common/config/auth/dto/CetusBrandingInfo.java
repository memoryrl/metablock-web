package kware.common.config.auth.dto;

import kware.apps.manager.cetus.menu.dto.response.SessionMenuList;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class CetusBrandingInfo implements Serializable {

    private final String rootLogo;
    private final String footerLogo;
    private final String companyLogo;
    private final String leftSlide;
    private final String rightSlide;
    private final String title1;
    private final String title2;
    private final String title3;

    public CetusBrandingInfo(SessionMenuList rootMenu, SessionMenuList footerMenuRoot) {
        this.rootLogo = rootMenu.getLogoImgId();
        this.footerLogo = footerMenuRoot.getLogoImgId();
        this.companyLogo = footerMenuRoot.getCompanyLogoImgId();
        this.leftSlide = rootMenu.getLeftSlideImgId();
        this.rightSlide = rootMenu.getRightSlideImgId();
        this.title1 = footerMenuRoot.getTitle1();
        this.title2 = footerMenuRoot.getTitle2();
        this.title3 = footerMenuRoot.getTitle3();
    }
}
