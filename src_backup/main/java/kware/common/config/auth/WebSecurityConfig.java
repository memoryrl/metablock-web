package kware.common.config.auth;

import cetus.config.CetusConfig;
import kware.apps.manager.cetus.loginhist.service.CetusUserLoginHistService;
import kware.apps.manager.cetus.menu.service.CetusMenuInfoService;
import kware.apps.manager.cetus.user.service.CetusUserService;
import kware.common.config.IpWhoService;
import kware.common.config.auth.handler.*;
import kware.common.config.support.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final ApplicationContext applicationContext;
    private static final String LOGIN = "/login";
    private final SessionRegistry sessionRegistry;
    private final CetusConfig config;

    @Bean
    public PrincipalDetailsService principalDetailsService(
            CetusUserService userService,
            CetusMenuInfoService menuService,
            MenuManager menuManager
    ) {
        return new PrincipalDetailsService(userService, menuService, menuManager);
    }

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    @Bean
    public MyDaoAuthenticationProvider myDaoAuthenticationProvider(PrincipalDetailsService detailsService, BCryptPasswordEncoder encoder) {
        MyDaoAuthenticationProvider provider = new MyDaoAuthenticationProvider();
        provider.setUserDetailsService(detailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
    public CustomLoginSuccessHandler loginSuccessHandler(CetusUserService userService, CetusUserLoginHistService histService, IpWhoService ipWhoService) {
        return new CustomLoginSuccessHandler(userService, histService, ipWhoService);
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomLoginFailuerHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(
                HttpSecurity security,
                MyDaoAuthenticationProvider provider,
                CustomLoginSuccessHandler successHandler
    ) throws Exception {

        configureDevOrLocalSecurity(security, provider);

        security
            .authorizeRequests(this::authorizeRequests)
            .exceptionHandling(this::exception)
            .formLogin(formLoginConfigurer -> formLogin(formLoginConfigurer, successHandler))
            .logout(this::logout)
            .sessionManagement(this::sessionManagement);
        return security.build();
    }

    // url 보안 설정
    private void authorizeRequests(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry) {
        urlRegistry
            .antMatchers(Paths.EXCLUDE_URL).permitAll()
            .anyRequest().authenticated();
    }

    // 보안 설정
    private void configureDevOrLocalSecurity(
                    HttpSecurity httpSecurity,
                    MyDaoAuthenticationProvider myDaoAuthenticationProvider
    ) throws Exception {
        if(isLocalProfile()) {
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(AbstractHttpConfigurer::disable)
                    .headers(headersConfigurer ->
                            headersConfigurer
                                    .frameOptions(frameOptions -> frameOptions.sameOrigin())
                                    .xssProtection(xss -> xss.xssProtectionEnabled(true))
                    )
                    .authenticationProvider(myDaoAuthenticationProvider);
        } else {
            httpSecurity
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
                    .headers(headersConfigurer ->
                            headersConfigurer
                                    .frameOptions(frameOptions -> frameOptions.sameOrigin())  // iframe 허용 (same origin만)
                                    .xssProtection(xss -> xss.xssProtectionEnabled(true))     // XSS 방어 활성화
                    );
        }
    }

    // 권한 페이지 설정
    private void exception(ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling) {
        exceptionHandling
                .authenticationEntryPoint(new CustomAuthEntryPoint(LOGIN));
    }

    // formLogin 설정
    private void formLogin(FormLoginConfigurer<HttpSecurity> formLogin, CustomLoginSuccessHandler loginSuccessHandler)  {
        formLogin
            .loginPage(LOGIN) // 로그인 페이지 링크
            .usernameParameter("userId")
            .loginProcessingUrl("/loginProc")
            .successHandler(loginSuccessHandler)
            .failureHandler(customAuthenticationFailureHandler());
    }

    // logout 설정
    private void logout(LogoutConfigurer<HttpSecurity> logout) {
        logout
            .logoutUrl("/logout")
            .logoutSuccessUrl(LOGIN) // 로그아웃 성공시 연결되는 주소
            .invalidateHttpSession(true) // 로그아웃시 저장해 둔 세션 제거
            .clearAuthentication(true);
    }

    // 중복 로그인 설정
    private void sessionManagement(SessionManagementConfigurer<HttpSecurity> manage) {
        manage
            .sessionFixation().changeSessionId()
            .maximumSessions(config.getMaxSession())
            .maxSessionsPreventsLogin(true)
            .sessionRegistry(sessionRegistry)
            .expiredUrl(LOGIN);
    }

    // cors 설정
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private boolean isLocalProfile() {
        List<String> profiles = Arrays.asList(applicationContext.getEnvironment().getActiveProfiles());
        return profiles.contains("local");
    }
}
