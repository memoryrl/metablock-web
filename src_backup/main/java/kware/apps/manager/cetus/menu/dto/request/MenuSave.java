package kware.apps.manager.cetus.menu.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuSave {
    private String authorCd;
    private Long upperMenuNo;
    private String menuNm;
    private int sortNo;
}
