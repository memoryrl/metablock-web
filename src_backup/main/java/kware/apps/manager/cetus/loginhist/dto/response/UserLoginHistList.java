package kware.apps.manager.cetus.loginhist.dto.response;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginHistList {

    private String userUid;
    private String loginDt;
    private String loginIp;
    private String loginRegion;
    private String loginBrowser;
    private String loginAccessUrl;
    private String sessionId;

}
