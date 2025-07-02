package kware.apps.manager.cetus.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserListSearch {

    private String userNm;
    private String userId;
    private String startDate;
    private String endDate;
    private String[] userAuthorCd;
    private String[] userStatus;
    private Long[] userGroup;
    private Long[] userDepth;
    private Long[] userPosition;

    private Long workplaceUid;

}
