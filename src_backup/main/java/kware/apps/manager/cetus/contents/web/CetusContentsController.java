package kware.apps.manager.cetus.contents.web;


import kware.apps.asp.contents.service.CetusContentsService;
import kware.apps.manager.cetus.form.dto.request.FormGroup;
import kware.apps.manager.cetus.form.dto.response.ElementType;
import kware.apps.manager.cetus.form.service.CetusFormColumnsService;
import kware.common.config.auth.MenuNavigationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager/cetus/contents")
public class CetusContentsController {

    private final CetusContentsService service;
    private final MenuNavigationManager menuNavigationManager;

    @GetMapping
    public String list(Model model) {
        menuNavigationManager.renderingPage("/manager/cetus/contents", "컨텐츠 관리", true, model);
        return "manager/contents/index";
    }

    @GetMapping("/save")
    public String save(Model model) {
        menuNavigationManager.renderingPage("/manager/cetus/contents", "컨텐츠 등록", false, model);
        return "manager/contents/save";
    }

    @GetMapping("/{uid}")
    public String form(@PathVariable Long uid, Model model) {
        menuNavigationManager.renderingPage("/manager/cetus/contents", "컨텐츠 수정", false, model);
        model.addAttribute("content", service.view(uid));
        return "manager/contents/form";
    }

}
