package kware.apps.manager.cetus.enumstatus;


import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum MenuRootCd {

    TOP_ROOT("상단 메뉴 루트"),
    FOOTER_ROOT("푸터 메뉴 루트");

    private String description;

    MenuRootCd(String description) {
        this.description = description;
    }

    public static List<EnumCodeDto> toList() {
        return Arrays.stream(values())
                .map(e -> new EnumCodeDto(e.name(), e.description))
                .collect(Collectors.toList());
    }
}
