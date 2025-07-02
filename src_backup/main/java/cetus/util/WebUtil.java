package cetus.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

public final class WebUtil {

    private WebUtil() {}

    
    /**
     * 현재 스레드에 대한 HttpServletRequest를 획득
     */
    public static HttpServletRequest getCurrentRequest() {

        try {
            return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
    
        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
    
        return ip;
    }
    
    //모바일 브라우저 요청인지 판단
    public static boolean isMobileBrowser(String userAgent) {

        return userAgent.matches(".*(iPhone|iPod|LG|SAMSUNG|Samsung|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson).*");
    }
    
    //아이폰 계열 모바일 브라우저 요청인지 판단
    public static boolean isMobileIphoneBrowser(String userAgent) {

        return userAgent.matches(".*(iPhone|iPod).*");
    }
    
    /**
     * Get spring bean object from context
     */
    public static Object getSpringBeanFromContext(HttpServletRequest request, String beanName) {
        return getSpringBean(request, beanName);
    }
    public static Object getSpringBean(HttpServletRequest request, String beanName) {
        
        WebApplicationContext applicationContext = 
            WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        
        if (applicationContext != null && applicationContext.containsBean(beanName)) {
            return applicationContext.getBean(beanName);
        }
        
        return null;
    }
}