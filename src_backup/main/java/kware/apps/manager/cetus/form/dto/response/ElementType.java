package kware.apps.manager.cetus.form.dto.response;

import lombok.Getter;

import java.util.EnumSet;

@Getter
public enum ElementType {
    TEXT, TEXTAREA, NUMBER, EMAIL, TEL, DATE,
    CHECKBOX, RADIO, SELECT, MULTISELECT, FILE, IMAGE;

    public boolean requiresOption() {
        return EnumSet.of(SELECT, RADIO, CHECKBOX, MULTISELECT).contains(this);
    }

    public static ElementType from(String value) {
        try {
            return ElementType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("지원하지 않는 ElementType입니다: " + value);
        }
    }
}
