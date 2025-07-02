package kware.apps.manager.cetus.form.web;


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
@RequestMapping("/asp/cetus/form")
public class CetusFormColumnsController {

    private final CetusFormColumnsService service;
    private final MenuNavigationManager menuNavigationManager;

    @GetMapping
    public String list(Model model) {
        menuNavigationManager.renderingPage("/asp/cetus/form", "폼 관리", true, model);
        model.addAttribute("formGroups", FormGroup.values());
        return "asp/form/index";
    }

    @GetMapping("/dual")
    public String dual(Model model) {
        menuNavigationManager.renderingPage("/asp/cetus/form/dual", "폼 관리", true, model);
        model.addAttribute("types", ElementType.values());
        model.addAttribute("formGroups", FormGroup.values());
        return "asp/form/dual";
    }
    @GetMapping("/save")
    public String save(Model model) {
        model.addAttribute("types", ElementType.values());
        model.addAttribute("formGroups", FormGroup.values());
        menuNavigationManager.renderingPage("/asp/cetus/form", "폼 컬럼 등록", false, model);
        return "asp/form/save";
    }

    @GetMapping("/{uid}")
    public String form(@PathVariable Long uid, Model model) {
        model.addAttribute("types", ElementType.values());
        model.addAttribute("obj", service.column(uid));
        model.addAttribute("uid", uid);
        menuNavigationManager.renderingPage("/asp/cetus/form", "폼 컬럼 수정", false, model);
        return "asp/form/form";
    }


    @GetMapping("/modal-preview")
    public String getModalContent(String formGroup, Integer rowSize, Model model) {
        model.addAttribute("fields",  service.getFormGroupColumns(formGroup)); // Thymeleaf에 넘길 모델 변수
        model.addAttribute("rowSize", rowSize);
        return "fragments/formbuilder :: #formFields"; // modal.html 안의 #previewModal fragment만 렌더링
    }

}
