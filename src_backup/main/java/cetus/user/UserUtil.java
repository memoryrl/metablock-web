package cetus.user;

import cetus.util.WebUtil;
import kware.common.config.auth.dto.SessionUserInfo;
import org.springframework.security.core.Authentication;
import javax.servlet.http.HttpServletRequest;

public final class UserUtil {
    public static final String REQUEST_USER_KEY = "user";
    
    /**
     * 로그인되어 있다면 CetusUser 아니면 null 반환
     * @return {@link SessionUserInfo}
     */
    public static SessionUserInfo getUser() {
        return getUser(WebUtil.getCurrentRequest());
    }

    public static Long getUserWorkplaceUid() {
        return getUser().getWorkplaceUid();
    }
    
    public static SessionUserInfo getUser(HttpServletRequest request) {
        Object user = request.getAttribute(REQUEST_USER_KEY);
        return user != null ? (SessionUserInfo) user : null;
    }
    
    public static boolean isAuthenticated(Authentication authentication) {
        return authentication != null &&
               authentication.getPrincipal() != "anonymousUser";
    }
}
