package kware.apps.manager.cetus.contents.categories;
import java.util.List;

import kware.apps.manager.cetus.enumstatus.EnumCodeDto;

public class DateRange {

    public static final String FIELD = "DateRange";
    public static final String FIELDNAME = "날짜 범위";

    public static final List<EnumCodeDto> ITEMS = List.of(
        new EnumCodeDto("airDate", "방송 방영일 기준"),
        new EnumCodeDto("includeRerun", "재방송 포함")
    );
}
