package kware.apps.manager.cetus.contents.categories;
import java.util.List;

import kware.apps.manager.cetus.enumstatus.EnumCodeDto;

public class ContentRatings {

    public static final String FIELD = "ContentRatings";
    public static final String FIELDNAME = "이용 등급";

    public static final List<EnumCodeDto> ITEMS = List.of(
        new EnumCodeDto("all", "모든 연령"),
        new EnumCodeDto("12plus", "12세 이상"),
        new EnumCodeDto("15plus", "15세 이상"),
        new EnumCodeDto("19plus", "19세 이상")
    );
}
