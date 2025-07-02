package kware.apps.manager.cetus.contents.categories;

import java.util.List;

import kware.apps.manager.cetus.enumstatus.EnumCodeDto;

public class ContentFormat {

    public static final String FIELD = "ContentFormat";
    public static final String FIELDNAME = "콘텐츠 형식";

    public static final List<EnumCodeDto> ITEMS = List.of(
        new EnumCodeDto("regular", "정규 방송"),
        new EnumCodeDto("special", "특집 방송"),
        new EnumCodeDto("clip", "클립 영상"),
        new EnumCodeDto("interview", "인터뷰"),
        new EnumCodeDto("live", "라이브 스트리밍")
    );
}
