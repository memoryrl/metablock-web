package kware.apps.manager.cetus.bbsctt.web;

import kware.apps.manager.cetus.answer.dto.response.AnswerList;
import kware.apps.manager.cetus.answer.service.CetusBbscttAnswerService;
import kware.apps.manager.cetus.bbs.domain.CetusBbs;
import kware.apps.manager.cetus.bbs.service.CetusBbsService;
import kware.apps.manager.cetus.bbsctt.domain.CetusBbsctt;
import kware.apps.manager.cetus.bbsctt.dto.response.BbscttView;
import kware.apps.manager.cetus.bbsctt.service.CetusBbscttService;
import kware.common.config.auth.MenuNavigationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/asp/cetus/bbsctt")
public class CetusBbscttController {

    private final CetusBbscttService bbscttService;
    private final CetusBbsService bbsService;
    private final CetusBbscttAnswerService answerService;
    private final MenuNavigationManager menuNavigationManager;

    @GetMapping("/{bbsUid}")
    public String index(@PathVariable("bbsUid") Long bbsUid, final Model model) {
        CetusBbs bbs = bbsService.view(bbsUid);
        model.addAttribute("bbs", bbs);
        menuNavigationManager.renderingPage("/asp/cetus/bbsctt/"+bbsUid, bbs.getBbsNm(), true, model);
        return "asp/bbsctt/index";
    }

    @GetMapping("/save/{bbsUid}")
    public String save(@PathVariable("bbsUid") Long bbsUid, final Model model) {
        CetusBbs bbs = bbsService.view(bbsUid);
        model.addAttribute("bbs", bbs);
        menuNavigationManager.renderingPage("/asp/cetus/bbsctt/"+bbsUid, "게시글 등록", false, model);
        return "asp/bbsctt/save";
    }

    @GetMapping("/view/{bbscttUid}")
    public String view(@PathVariable("bbscttUid") Long bbscttUid, final Model model, HttpServletRequest req, HttpServletResponse res) {
        BbscttView bbsctt = bbscttService.findViewByBbscttUid(bbscttUid);
        model.addAttribute("bbsctt", bbsctt);

        CetusBbs bbs = bbsService.view(bbsctt.getBbsUid());
        model.addAttribute("bbs", bbs);
        bbscttService.increaseViewCount(bbscttUid, req, res);

        List<AnswerList> answers = answerService.findAllAnswerList(bbscttUid);
        model.addAttribute("answers", answers);

        menuNavigationManager.renderingPage("/asp/cetus/bbsctt/"+bbs.getBbsUid(), "게시글 조회", false, model);

        return "asp/bbsctt/view";
    }

    @GetMapping("/form/{bbscttUid}")
    public String form(@PathVariable("bbscttUid") Long bbscttUid, final Model model) {
        CetusBbsctt bbsctt = bbscttService.view(bbscttUid);
        model.addAttribute("bbsctt", bbsctt);

        CetusBbs bbs = bbsService.view(bbsctt.getBbsUid());
        model.addAttribute("bbs", bbs);

        menuNavigationManager.renderingPage("/asp/cetus/bbsctt/"+bbs.getBbsUid(), "게시글 수정", false, model);
        return "asp/bbsctt/form";
    }
}
