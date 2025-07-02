package kware.apps.manager;

import cetus.Response;
import kware.apps.manager.cetus.user.dto.response.UserFullInfo;
import kware.apps.manager.cetus.user.service.CetusUserService;
import kware.common.config.auth.dto.SessionUserInfo;
import kware.common.config.session.SessionHelper;
import kware.common.config.session.SessionStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    private final CetusUserService cetusUserService;
    private final SessionHelper helper;
    private final SessionStore store;

    @GetMapping("")
    public String index() {
        return "redirect:/asp";
    }

    @GetMapping("login")
    public String login(Model model) {
        model.addAttribute("storeType", store.getStoreType());
        return "login";
    }

    @GetMapping("/sessionExpired")
    public String sessionExpired() {
        return "sessionExpired";
    }

    @ResponseBody
    @PostMapping("/login/force.json")
    public Response forceLogin(HttpServletResponse response, HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        UserFullInfo user = cetusUserService.findUserByUserId(userId);

        SessionUserInfo sessionUserInfo = new SessionUserInfo(user);
        session.removeAttribute("userId");
        helper.findSessionAndExpired(sessionUserInfo, response);
        Authentication authentication = helper.initializeAuthentication(sessionUserInfo);
        helper.createSessionAndEditSecurityContext(authentication, request);
        response.sendRedirect("/asp/home");
        return Response.ok();
    }

}
