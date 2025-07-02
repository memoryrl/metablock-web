package cetus.config;

import cetus.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
@Setter
@Component
@ConfigurationProperties("cetus")
public class CetusConfig {

    private String allowedOrigin;
    private String mybatisConfig;

    private Logging logging = new Logging();
    private Login login = new Login();
    private Map<String, Datasource> datasource = new HashMap<>();
    private String downloadPath;
    private String summernotePath;
    private String ckeditorPath;
    private String recordPath;
    private String baseUrl;
    private Integer maxSession = 1;

    /**
     * inner classes
     */

    @Setter
    @Getter
    @ToString
    public static class Logging {

        private boolean request;
        private boolean uri;
        private boolean params;
        private boolean controller;
        private boolean sql;
        private boolean view;
        private boolean elapsed;
        private String[] excludeUrls;
        private String[] excludeParams;

        public void setExcludeUrls(String urls) {
            if ( StringUtil.isNotEmpty(urls) ) {
                this.excludeUrls = StringUtil.commaToArray(urls);
            }
        }

        public void setExcludeParams(String params) {
            if ( StringUtil.isNotEmpty(params) ) {
                this.excludeParams = StringUtil.commaToArray(params);
            }
        }

        public void all() {
            setUri(true);
            setRequest(true);
            setSql(true);
            setController(true);
            setParams(true);
            setElapsed(true);
            setView(true);
        }
    }

    @Getter
    @Setter
    @ToString
    public static class Login {
        private boolean exceed; // 로그인 시도 제한 사용 여부
        private Integer recount; // 로그인 제한 횟수
        private Integer retime; // 로그인 제한시간(초)
        private boolean multi; // 중복 로그인 허용 여부
        private boolean exitOldSession; // 중복 로그인 시 이전 세션 강제 종료 여부
        private boolean logging; // 접속자 로그 기록 여부
    }

    @Getter
    @Setter
    public static class Datasource {
        private String type;
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private String mapperPattern;
        private int maxPoolSize = 10;
    }
}
