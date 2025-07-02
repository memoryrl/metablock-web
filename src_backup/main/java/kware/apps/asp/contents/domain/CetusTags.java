package kware.apps.asp.contents.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CetusTags {

    private Long contentsUid;
    private String tagName;
    private Long regUid;
    private String regDt;

}
