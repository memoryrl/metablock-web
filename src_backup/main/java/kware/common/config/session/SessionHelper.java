package kware.common.config.session;


import kware.common.config.auth.PrincipalDetails;
import kware.common.config.auth.PrincipalDetailsService;
import kware.common.config.auth.dto.SessionUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SessionHelper {
     private final SessionRegistry sessionRegistry;
     private final PrincipalDetailsService loginService;

     public Authentication initializeAuthentication(SessionUserInfo user) {
          loginService.setUserPermissionsAndMenus(user);
          Collection<GrantedAuthority> collect = new ArrayList<>();
          collect.add(user::getRole);
          Authentication authentication = new PreAuthenticatedAuthenticationToken(new PrincipalDetails(user), null, collect);
          SecurityContextHolder.getContext().setAuthentication(authentication);
          return authentication;
     }

     public void findSessionAndExpired(SessionUserInfo user, HttpServletResponse response) throws IOException {
          List<SessionInformation> si = sessionRegistry.getAllSessions(new PrincipalDetails(user), false);
          if (si != null && !si.isEmpty() ) {
               si.get(0).expireNow();
          } else {
               response.sendRedirect("/login?expired");
          }
     }

     public void createSessionAndEditSecurityContext(Authentication authentication, HttpServletRequest request) {
          HttpSession session = request.getSession(true);
          session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                  SecurityContextHolder.getContext());

          //SessionRegistry와 동기화 처리
          sessionRegistry.registerNewSession(session.getId(), authentication.getPrincipal());
     }
}
