package cetus.user;

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
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserUtil.isAuthenticated(authentication)) {
            PrincipalDetails details = (PrincipalDetails) authentication.getPrincipal();
            SessionUserInfo user = details.getUser();

            request.setAttribute(UserUtil.REQUEST_USER_KEY, user);
        }
        return true;
    }
}
