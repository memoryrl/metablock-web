package kware.apps.manager.cetus.enumstatus;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum BbsTpCd {

    NOTICE("공지사항"),
    BOARD("자유 게시판"),
    FAQ("faq"),
    QNA("1:1문의");

    private String subName;

    BbsTpCd(String subName) {
        this.subName = subName;
    }

    public static List<EnumCodeDto> toList() {
        return Arrays.stream(values())
                .map(e -> new EnumCodeDto(e.name(), e.getSubName()))
                .collect(Collectors.toList());
    }

    public static boolean isValidCode(String code) {
        return Arrays.stream(BbsTpCd.values())
                .anyMatch(status -> status.name().equals(code));
    }
}
