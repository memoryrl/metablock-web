package kware.apps.manager.cetus.group.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupList {

    private Long uid;
    private String name;

}
