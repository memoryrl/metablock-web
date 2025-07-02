package kware.apps.asp;


import cetus.user.UserUtil;
import cetus.util.DateTimeUtil;
import cetus.util.HtmlUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kware.apps.asp.contents.dto.response.ContentsView;
import kware.apps.asp.contents.service.CetusContentsService;
import kware.apps.manager.cetus.bbsctt.dto.response.BbscttRecentList;
import kware.apps.manager.cetus.bbsctt.service.CetusBbscttService;
import kware.apps.manager.cetus.form.service.CetusFormColumnsService;
import kware.apps.manager.cetus.user.dto.response.UserFullInfo;
import kware.apps.manager.cetus.user.service.CetusUserService;
import kware.common.config.auth.MenuNavigationManager;
import kware.common.config.auth.dto.SessionUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Controller
@RequestMapping("/asp")
@RequiredArgsConstructor
public class AspController {

    private final CetusUserService cetusUserService;
    private final CetusBbscttService bbscttService;
    private final CetusFormColumnsService columnsService;
    private final MenuNavigationManager menuNavigationManager;
    private final CetusContentsService contentsService;


    @GetMapping({"/home", "/", ""})
    public String home(Model model) {
        menuNavigationManager.renderingPage("/asp/home", "HOME", true, model);
        List<BbscttRecentList> recentBbsctt = bbscttService.findRecentBbsctt(5);
        recentBbsctt.forEach(recent -> {
            // 1. 게시글 내용
            String str = HtmlUtil.stripHtmlPreserveLines(recent.getBbscttCnt());
            String customCnt = (str.length() <= 70) ? str : str.substring(0, 70) + "...";
            recent.setBbscttCnt(customCnt);

            // 2. 날짜
            Map<String, String> map = DateTimeUtil.dateToEng(recent.getRegDt());
            recent.setMonthDay(map.get("month"), map.get("day"));
        });
        model.addAttribute("recentBbsctt", recentBbsctt);

        return "asp/page/home";
    }


    @GetMapping("/list")
    public String list(Model model) {
        menuNavigationManager.renderingPage("/asp/list", "LIST", true, model);
        model.addAttribute("userUid", UserUtil.getUser().getUid());
        return "asp/page/list";
    }

    @GetMapping("/detail/{uid}")
    public String openDetail(@PathVariable("uid") Long uid, Model model) {
        menuNavigationManager.renderingPage("/asp/list", "Detail", false, model);
        model.addAttribute("userUid", UserUtil.getUser().getUid());
        ContentsView content = contentsService.view(uid);
        model.addAttribute("content", content);
        return "asp/page/detail";
    }

    @GetMapping("/myInfo")
    public String myInfo(Model model) {
        SessionUserInfo user = UserUtil.getUser();
        UserFullInfo info = cetusUserService.findUserFullInfoByUserUid(user.getUid());
        model.addAttribute("view", info);
        model.addAttribute("fields", columnsService.getFormGroupColumns("SIGNUP"));

        String metaData = info.getMetaData();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            if (metaData != null && !metaData.trim().isEmpty()) {
                map = objectMapper.readValue(metaData, new TypeReference<Map<String, Object>>() {});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("metadata", map);
        menuNavigationManager.renderingPage("/asp/myInfo", "내 정보 수정", true, model);

        return "asp/myInfo/index";
    }

    @GetMapping("/chart")
    public String chart(Model model) {
        menuNavigationManager.renderingPage("/asp/chart", "관계데이터 다차원 탐색", true, model);
        return "asp/page/chart";
    }

    @GetMapping("/signup")
    public String signup(HttpSession session, Model model) {
        Boolean isInvited = (Boolean) session.getAttribute("isInvited");
        String inviteToken = (String) session.getAttribute("inviteToken");
        String inviteEmail = (String) session.getAttribute("inviteEmail");

        model.addAttribute("isInvited", isInvited != null && isInvited);
        model.addAttribute("inviteToken", inviteToken);
        model.addAttribute("inviteEmail", inviteEmail);
        model.addAttribute("fields", columnsService.getFormGroupColumns("SIGNUP"));

        return "/signup";
    }

    @GetMapping("/expired")
    public String expired() {
        return "asp/page/expired";
    }
}
