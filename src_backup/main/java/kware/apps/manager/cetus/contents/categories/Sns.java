package kware.apps.manager.cetus.contents.categories;
import java.util.List;

import kware.apps.manager.cetus.enumstatus.EnumCodeDto;

public class Sns {

    public static final String FIELD = "Sns";
    public static final String FIELDNAME = "SNS";

    public static final List<EnumCodeDto> ITEMS = List.of(
        new EnumCodeDto("playtalk", "플레이톡"),
        new EnumCodeDto("youtube", "유튜브"),
        new EnumCodeDto("instagram", "인스타그램")
    );
}
