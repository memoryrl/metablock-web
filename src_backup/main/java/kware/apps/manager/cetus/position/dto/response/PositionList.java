package kware.apps.manager.cetus.position.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PositionList {

    private Long uid;
    private String name;
}
