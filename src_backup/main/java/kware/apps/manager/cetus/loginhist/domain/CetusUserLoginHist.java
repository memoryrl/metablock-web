package kware.apps.manager.cetus.loginhist.domain;

import kware.apps.manager.cetus.loginhist.dto.request.UserLoginHistSave;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusUserLoginHist {

    private Long userUid;
    private String loginDt;
    private String loginIp;
    private String loginBrowser;
    private String loginRegion;
    private String loginAccessUrl;
    private String sessionId;

    public CetusUserLoginHist(UserLoginHistSave request) {
        this.userUid = request.getUserUid();
        this.loginIp = request.getLoginIp();
        this.loginBrowser = request.getLoginBrowser();
        this.loginRegion = request.getLoginRegion();
        this.loginAccessUrl = request.getLoginAccessUrl();
        this.sessionId = request.getSessionId();
    }
}
