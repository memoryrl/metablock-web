package kware.apps.manager.cetus.menu.web;


import kware.apps.manager.cetus.enumstatus.UserAuthorCd;
import kware.common.config.auth.MenuNavigationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager/cetus/menu")
public class CetusMenuInfoController {

    private final MenuNavigationManager menuNavigationManager;

    @GetMapping({"", "/"})
    public String index(Model model) {
        model.addAttribute("userAuthorCd", UserAuthorCd.toList());
        menuNavigationManager.renderingPage("/manager/cetus/menu", "메뉴 관리", true, model);
        return "manager/menu/index";

    }
}
