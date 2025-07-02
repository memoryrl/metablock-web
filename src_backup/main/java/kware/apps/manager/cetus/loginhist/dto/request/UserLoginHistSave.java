package kware.apps.manager.cetus.loginhist.dto.request;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginHistSave {

    private Long userUid;
    private String loginIp;
    private String loginBrowser;
    private String loginRegion;
    private String loginAccessUrl;
    private String sessionId;

    @Builder(builderMethodName = "insertLoginHist")
    public UserLoginHistSave(Long userUid, String loginIp, String loginBrowser, String loginRegion, String loginAccessUrl, String sessionId) {
        this.userUid = userUid;
        this.loginIp = loginIp;
        this.loginBrowser = loginBrowser;
        this.loginRegion = loginRegion;
        this.loginAccessUrl = loginAccessUrl;
        this.sessionId = sessionId;
    }
}
