package kware.common.config.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
public class CustomAuthEntryPoint extends LoginUrlAuthenticationEntryPoint {
    public CustomAuthEntryPoint(String loginUrl) {
        super(loginUrl);
    }

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException)
            throws IOException, ServletException {
        String ajaxHeader = req.getHeader("X-Requested-With");
        // AJAX 요청인지 검사 (헤더 검사, 비동기인지 체크)
        boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);
        if (isAjax) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "세션 만료로 인해서 거부되었습니다.");
        } else {
            super.commence(req, res, authException);
        }
    }
}
