package kware.apps.manager.cetus.contents.categories;
import java.util.List;

import kware.apps.manager.cetus.enumstatus.EnumCodeDto;

public class Language {

    public static final String FIELD = "Language";
    public static final String FIELDNAME = "언어";

    public static final List<EnumCodeDto> ITEMS = List.of(
        new EnumCodeDto("ko", "한국어"),
        new EnumCodeDto("en", "영어"),
        new EnumCodeDto("ja", "일본어"),
        new EnumCodeDto("zh", "중국어")
    );
}
