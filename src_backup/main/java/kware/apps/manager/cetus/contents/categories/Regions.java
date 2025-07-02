package kware.apps.manager.cetus.contents.categories;
import java.util.List;

import kware.apps.manager.cetus.enumstatus.EnumCodeDto;

public class Regions {

    public static final String FIELD = "Regions";
    public static final String FIELDNAME = "국가/지역";

    public static final List<EnumCodeDto> ITEMS = List.of(
        new EnumCodeDto("kr", "대한민국"),
        new EnumCodeDto("us", "미국"),
        new EnumCodeDto("eu", "유럽"),
        new EnumCodeDto("asia", "아시아")
    );
}
