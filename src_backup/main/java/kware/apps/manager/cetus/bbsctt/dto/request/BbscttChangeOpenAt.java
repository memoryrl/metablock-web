package kware.apps.manager.cetus.bbsctt.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BbscttChangeOpenAt {

    private Long[] uids;
    private String openAt;
}
