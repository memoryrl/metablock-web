package kware.common.config.filter;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.MultipartFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MultiPartFilterConfig {

     @Bean
     public FilterRegistrationBean<MultipartFilter> multipartFilterRegistrationBean() {
         FilterRegistrationBean<MultipartFilter> registrationBean = new FilterRegistrationBean<>();
         registrationBean.setFilter(new MultipartFilter());
         registrationBean.addUrlPatterns("/*");
         registrationBean.setOrder(1);
         registrationBean.setInitParameters(getMultipartFilterInitParams());
         return registrationBean;
     }

     private Map<String, String> getMultipartFilterInitParams() {
         Map<String, String> initParams = new HashMap<>();
         initParams.put("multipartResolverBeanName", "multipartResolver");
         return initParams;
     }
}
