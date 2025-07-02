package kware.apps.manager.cetus.user.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserView {

    private Long uid;
    private String userId;
    private String password;
    private String userNm;
    private String authorCd;
    private String status;
    private Integer failCnt;
    private Long profileUid;
    private String metaData;
    private String useAt;
    private String approveAt;
    private String userEmail;
}
