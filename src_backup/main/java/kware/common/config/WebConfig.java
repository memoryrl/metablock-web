package kware.common.config;

import java.util.concurrent.TimeUnit;

import kware.common.validator.ValidMessageInterpolator;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import cetus.config.CetusConfig;
import cetus.log.LoggingInterceptor;
import cetus.menu.MenuInterceptor;
import cetus.user.UserInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CetusConfig configs;
    private final LocaleChangeInterceptor localeChangeInterceptor;
    private final ObjectMapper objectMapper;

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setMessageInterpolator(new ValidMessageInterpolator());
        return validatorFactoryBean;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/ckImage/**")
                .addResourceLocations("file:///" + configs.getCkeditorPath());

        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
                .resourceChain(true)
                .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
    }

    @Bean
    public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
        return new ResourceUrlEncodingFilter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        loggingInterceptor().setLogger(LoggerFactory.getLogger(LoggingInterceptor.class));
        loggingInterceptor().setLogging(configs.getLogging());
        registry.addInterceptor(loggingInterceptor()).excludePathPatterns("/assets/**/*", "/swagger-ui/**", "/swagger-resources/**", "/cetus/files/download/**");
        registry.addInterceptor(userInterceptor()).excludePathPatterns("/assets/**/*", "/login", "/loginProc", "/logout", "/error/**/*");
        registry.addInterceptor(localeChangeInterceptor);
    }

    @Bean
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor();
    }

    @Bean
    public UserInterceptor userInterceptor() {
        return new UserInterceptor();
    }
    @Bean
    public MenuInterceptor menuInterceptor() {
        return new MenuInterceptor();
    }
}
