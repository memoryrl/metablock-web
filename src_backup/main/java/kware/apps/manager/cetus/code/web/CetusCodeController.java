package kware.apps.manager.cetus.code.web;


import kware.apps.manager.cetus.code.service.CetusCodeService;
import kware.common.config.auth.MenuNavigationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/manager/code")
@RequiredArgsConstructor
public class CetusCodeController {

    private final CetusCodeService service;
    private final MenuNavigationManager menuNavigationManager;

    @GetMapping({"/list", "", "/"})
    public String list(Model model) {
        menuNavigationManager.renderingPage("/manager/code", "코드 관리", true, model);
        return "manager/code/index";
    }

    @GetMapping({"/save"})
    public String save(Model model) {
        menuNavigationManager.renderingPage("/manager/code", "코드 등록", false, model);
        return "manager/code/save";
    }

    @RequestMapping(value = "/form", method = {RequestMethod.GET, RequestMethod.POST})
    public String form(@RequestParam(value = "code", required = false, defaultValue = "") String code,
                       @RequestParam(value = "uid", required = false, defaultValue = "") Long uid, Model model) {

        model.addAttribute("view", service.code(uid));
        model.addAttribute("childList", service.getChildCodes(code, ""));
        model.addAttribute("code", code);
        model.addAttribute("uid", uid);
        menuNavigationManager.renderingPage("/manager/code", "코드 수정", false, model);

        return "manager/code/form";
    }
}
