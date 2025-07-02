package kware.common.config.auth.handler;

import kware.apps.manager.cetus.enumstatus.UserStatus;
import kware.apps.manager.cetus.user.domain.CetusUser;
import kware.apps.manager.cetus.user.dto.response.UserFullInfo;
import kware.apps.manager.cetus.user.service.CetusUserService;
import kware.common.config.support.LoginErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
public class CustomLoginFailuerHandler implements AuthenticationFailureHandler {

    @Autowired
    private CetusUserService cetusUserService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException exception) throws IOException, ServletException {
        log.info("CustomAuthenticationFailuerHandler::onAuthenticationFailure");
        log.info("exception: {}", exception.getClass().getSimpleName());
        String exceptionMessage = messageAndException(exception);

        String userId = req.getParameter("userId");


        if (LoginErrorMessage.BadCredentialsException.name().equals(exception.getClass().getSimpleName())) {
            int failCount = failCnt(userId);
            if (failCount < 5)
                messageView(res, String.format("%s / 5회 실패하셨습니다. ", failCount) + "\n" + exceptionMessage);
            else messageView(res, LoginErrorMessage.LockedException.getValue());
        } else if (exception instanceof SessionAuthenticationException) {
            HttpSession session = req.getSession();
            session.setAttribute("userId", userId);
            res.sendRedirect("/login?session");
        } else if (exception instanceof DisabledException) {

            String userStatus = userStatus(userId);
            String message = (UserStatus.STOP.toString().equals(userStatus)) ? LoginErrorMessage.DisabledException_STOP.getValue() : LoginErrorMessage.DisabledException_WAIT.getValue();
            messageView2(res, userId, message);

        } else {
            messageView(res, exceptionMessage);
        }
    }

    private String userStatus(String userId) {
        CetusUser user = cetusUserService.viewById(userId);
        return user.getStatus();
    }

    private int failCnt(String userId) {
        UserFullInfo user = cetusUserService.findUserByUserId(userId);
        if (user.getFailCnt() < 4) cetusUserService.changeFailCnt(userId);
        else cetusUserService.changeUseAt(userId);
        return user.getFailCnt() + 1;
    }

    private void messageView(HttpServletResponse response, String exceptionName) throws IOException, ServletException {
        String encodedErrorMsg = URLEncoder.encode(exceptionName, "UTF-8");
        response.sendRedirect("/login?error=" + encodedErrorMsg);
    }

    private void messageView2(HttpServletResponse response, String userId, String message) throws IOException, ServletException {
        String encodedErrorMsg = URLEncoder.encode(message, "UTF-8");
        response.sendRedirect("/login?before=" + encodedErrorMsg+"&userId="+userId);
    }

    private String messageAndException(AuthenticationException exception) {
        LoginErrorMessage authenticationTypes = LoginErrorMessage.findOf(exception.getClass().getSimpleName());
        return authenticationTypes.getValue();
    }
}
