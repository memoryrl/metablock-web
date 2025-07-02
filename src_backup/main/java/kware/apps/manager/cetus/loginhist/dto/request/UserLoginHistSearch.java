package kware.apps.manager.cetus.loginhist.dto.request;

import lombok.Getter;

@Getter
public class UserLoginHistSearch {

    private Long userUid;

    public UserLoginHistSearch(Long userUid) {
        this.userUid = userUid;
    }
}
