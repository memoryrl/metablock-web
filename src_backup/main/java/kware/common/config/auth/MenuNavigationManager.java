package kware.common.config.auth;

import cetus.user.UserUtil;
import kware.apps.manager.cetus.menu.domain.CetusMenu;
import kware.apps.manager.cetus.program.dto.response.ProgrmFullInfo;
import kware.apps.manager.cetus.program.service.CetusProgrmInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MenuNavigationManager {

    private final CetusProgrmInfoService progrmInfoService;

    public void renderingPage(String parentMenuUrl, String currentPageName, boolean isTopMenu, final Model model) {

        List<CetusMenu> menusTop = UserUtil.getUser().getMenusTop();

        // 1. 네비게이션 경로 만들기
        List<String> navPath = new ArrayList<>();
        buildNavigationPath(menusTop, parentMenuUrl, navPath);
        if (!isTopMenu) {
            navPath.add(currentPageName);
        }

        // 2. 부모 메뉴 찾기
        CetusMenu parentMenu = findMenuByUrl(menusTop, parentMenuUrl);

        // 3. 이미지 스타일 정보 꺼내기
        String leftSlideImgId = "";
        String rightSlideImgId = "";
        if (parentMenu == null && parentMenuUrl != null) {
            ProgrmFullInfo programBean = progrmInfoService.findProgramFullInfoByUrl(parentMenuUrl);
            if(programBean == null) {
                parentMenu = getDefaultMenu(menusTop);
                leftSlideImgId = parentMenu.getLeftSlideImgId();
                rightSlideImgId = parentMenu.getRightSlideImgId();
            } else {
                leftSlideImgId = programBean.getLeftSlideImgId();
                rightSlideImgId = programBean.getRightSlideImgId();
            }
        } else {
            leftSlideImgId = parentMenu.getLeftSlideImgId();
            rightSlideImgId = parentMenu.getRightSlideImgId();
        }

        String centerLeftImage = (leftSlideImgId != null) ? leftSlideImgId : UserUtil.getUser().getBrandingInfo().getLeftSlide();
        String centerRightImage = (rightSlideImgId != null) ? rightSlideImgId : UserUtil.getUser().getBrandingInfo().getRightSlide();

        // 4. 뷰에 전달
        model.addAttribute("parentMenuUrl", parentMenuUrl);
        model.addAttribute("currentPageName", currentPageName);
        model.addAttribute("navigation", String.join(" > ", navPath));
        model.addAttribute("centerLeftImage", centerLeftImage);
        model.addAttribute("centerRightImage", centerRightImage);
    }

    private CetusMenu findMenuByUrl(List<CetusMenu> menus, String url) {
        for (CetusMenu menu : menus) {
            if (menu.getUrl().equals(url)) {
                return menu;
            }
            if (menu.hasChild()) {
                CetusMenu found = findMenuByUrl(menu.getChildren(), url);
                if (found != null) return found;
            }
        }
        return null;
    }

    private boolean buildNavigationPath(List<CetusMenu> menus, String targetUrl, List<String> path) {
        for (CetusMenu menu : menus) {
            path.add(menu.getMenuNm());
            if (menu.getUrl().equals(targetUrl)) {
                return true;
            }
            if (menu.hasChild()) {
                boolean found = buildNavigationPath(menu.getChildren(), targetUrl, path);
                if (found) return true;
            }
            path.remove(path.size() - 1);
        }
        return false;
    }

    private CetusMenu getDefaultMenu(List<CetusMenu> menus) {
        return (menus != null && !menus.isEmpty()) ? menus.get(0) : null;
    }
}
