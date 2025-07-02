package kware.apps.manager.cetus.user.dto.request;

import cetus.bean.FileBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile extends FileBean {

    private Long profileUid;
}
