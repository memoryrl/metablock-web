package cetus.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

@Configuration
@PropertySource(value="classpath:application-${spring.profiles.active:local}.yml", factory=YamlPropertyLoaderFactory.class, ignoreResourceNotFound=true)
public class DefaultConfig {
    
    @Bean
    LeaveaTrace leaveaTrace() {
        return new LeaveaTrace();
    }
    
    @Bean
    PathMatcher pathMatcher() {
        return new AntPathMatcher();
    }
    
    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }
}
