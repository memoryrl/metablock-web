package kware.apps.asp.contents.domain;

import java.util.List;

import kware.apps.manager.cetus.enumstatus.EnumCodeDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CetusCategories {

    private String field;
    private String fieldName;
    private List<EnumCodeDto> items;

}
