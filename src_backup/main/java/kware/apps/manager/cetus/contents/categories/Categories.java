package kware.apps.manager.cetus.contents.categories;

import java.util.List;

import kware.apps.manager.cetus.enumstatus.EnumCodeDto;

public class Categories {

    public static final String FIELD = "Categories";
    public static final String FIELDNAME = "카테고리";

    public static final List<EnumCodeDto> ITEMS = List.of(
        new EnumCodeDto("news", "뉴스"),
        new EnumCodeDto("drama", "드라마"),
        new EnumCodeDto("entertainment", "예능"),
        new EnumCodeDto("documentary", "다큐멘터리"),
        new EnumCodeDto("sports", "스포츠")
    );
}
