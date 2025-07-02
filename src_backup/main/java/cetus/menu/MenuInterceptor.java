package cetus.menu;

import cetus.user.UserUtil;
import kware.common.config.auth.PrincipalDetails;
import kware.common.config.auth.dto.SessionUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;


@Slf4j
public class MenuInterceptor implements HandlerInterceptor {

    private static final String REQUSET_MENU_KEY = "menu";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserUtil.isAuthenticated(authentication)) {
            PrincipalDetails details = (PrincipalDetails) authentication.getPrincipal();
            SessionUserInfo user = details.getUser();
            String uri = request.getRequestURI();
            if (!uri.equals("/")) {
                long includes = user.getAuthorizedMenuUrls().stream().filter(uri::contains).count();
                if (includes == 0) {
                    String message = URLEncoder.encode("잘못된 접근입니다.", "UTF-8");
                    response.sendRedirect("/?message=" + message);
                    return false;
                }
            }
            request.setAttribute(REQUSET_MENU_KEY, user.getAuthorizedMenuUrls());
        }
        return true;
    }
}
