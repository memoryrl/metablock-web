package cetus.bean;

import cetus.user.UserUtil;
import kware.common.config.auth.PrincipalDetails;
import kware.common.config.auth.dto.SessionUserInfo;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Getter
public class AuditBean {

    private Long regUid;
    private Long updtUid;
    private Long workplaceUid;

    public AuditBean() {
        this.workplaceUid = getWorkplaceUidFromSession();
    }

    public void setRegUid(Long regUid) {
        this.regUid = regUid;
    }
    public void setUpdtUid(Long updtUid) {
        this.updtUid = updtUid;
    }

    private Long getWorkplaceUidFromSession() {
        Long workplaceUid = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (UserUtil.isAuthenticated(authentication)) {
            PrincipalDetails details = (PrincipalDetails) authentication.getPrincipal();
            SessionUserInfo user = details.getUser();
            workplaceUid = user.getWorkplaceUid();
        }
        return workplaceUid;
    }
}
