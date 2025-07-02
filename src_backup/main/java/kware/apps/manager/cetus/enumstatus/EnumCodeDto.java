package kware.apps.manager.cetus.enumstatus;

import lombok.Getter;

@Getter
public class EnumCodeDto {

    private String code;
    private String name;

    public EnumCodeDto(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
