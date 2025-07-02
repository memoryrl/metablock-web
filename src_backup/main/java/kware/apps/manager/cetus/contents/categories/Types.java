package kware.apps.manager.cetus.contents.categories;
import java.util.List;

import kware.apps.manager.cetus.enumstatus.EnumCodeDto;

public class Types {

    public static final String FIELD = "Types";
    public static final String FIELDNAME = "타입";

    public static final List<EnumCodeDto> ITEMS = List.of(
        new EnumCodeDto("video", "동영상 (MP4, AVI…)"),
        new EnumCodeDto("subtitle", "자막파일 (SRT, TXT…)"),
        new EnumCodeDto("jsonXml", "JSON, XML"),
        new EnumCodeDto("image", "JPG, PNG")
    );
}
