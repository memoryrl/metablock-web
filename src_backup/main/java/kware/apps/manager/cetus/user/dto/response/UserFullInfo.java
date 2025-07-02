package kware.apps.manager.cetus.user.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFullInfo {

    private Long uid;
    private String userId;
    private String password;
    private String userNm;
    private String userEmail;
    private String role;
    private String roleNm;
    private String status;
    private String statusNm;
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
}
