package kware.apps.manager.cetus.contents.categories;
import java.util.List;

import kware.apps.manager.cetus.enumstatus.EnumCodeDto;

public class Sources {

    public static final String FIELD = "Sources";
    public static final String FIELDNAME = "출처";

    public static final List<EnumCodeDto> ITEMS = List.of(
        new EnumCodeDto("sbsNews", "SBS 뉴스팀"),
        new EnumCodeDto("sbsDrama", "SBS 드라마팀"),
        new EnumCodeDto("sbsEnter", "SBS 예능팀"),
        new EnumCodeDto("sbsSports", "SBS 스포츠국"),
        new EnumCodeDto("external", "외부 제작 콘텐츠")
    );
}
