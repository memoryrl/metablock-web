package kware.common.config.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kware.apps.manager.cetus.menu.domain.CetusMenu;
import kware.apps.manager.cetus.user.dto.response.UserFullInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@EqualsAndHashCode(of = "userId", callSuper = false)
public class SessionUserInfo implements Serializable {

    private Long uid;
    private String userId;
    @JsonIgnore
    private String password;
    private String userNm;
    private String userEmail;
    private String role;
    private String status;
    private Integer failCnt;
    private Long profileUid;
    private String profileId;
    private String metaData;
    private String useAt;
    private String approveAt;
    private Long workplaceUid;
    private String workplaceNm;
    private Long groupUid;
    private String groupNm;
    private Long deptUid;
    private String deptNm;
    private Long positionUid;
    private String positionNm;

    private List<String> authorizedMenuUrls;
    private List<CetusMenu> menusTop;
    private List<CetusMenu> menusFooter;
    private CetusBrandingInfo brandingInfo;

    public SessionUserInfo(UserFullInfo user) {
        this.uid = user.getUid();
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.userNm = user.getUserNm();
        this.userEmail = user.getUserEmail();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.failCnt = user.getFailCnt();
        this.profileUid = user.getProfileUid();
        this.profileId = user.getProfileId();
        this.metaData = user.getMetaData();
        this.useAt = user.getUseAt();
        this.approveAt = user.getApproveAt();

        this.workplaceUid = user.getWorkplaceUid();
        this.workplaceNm = user.getWorkplaceNm();
        this.groupUid = user.getGroupUid();
        this.groupNm = user.getGroupNm();
        this.deptUid = user.getDeptUid();
        this.deptNm = user.getDeptNm();
        this.positionUid = user.getPositionUid();
        this.positionNm = user.getPositionNm();
    }

    public void addMenuAndAuthorizedMenuUrls(List<CetusMenu> menusTop, List<CetusMenu> menusFooter, List<String> authorizedMenuUrls) {
        this.menusTop = menusTop;
        this.menusFooter = menusFooter;
        this.authorizedMenuUrls = authorizedMenuUrls;
    }

    public void addBrandingInfo(CetusBrandingInfo brandingInfo) {
        this.brandingInfo = brandingInfo;
    }

}
