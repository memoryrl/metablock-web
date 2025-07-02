package kware.apps.manager.cetus.enumstatus;


import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum UserStatus {

    APPROVED("사용중"),
    WAIT("승인 대기"),
    STOP("정지");

    private String description;

    UserStatus(String description) {
        this.description = description;
    }

    public static List<EnumCodeDto> toList() {
        return Arrays.stream(values())
                .map(e -> new EnumCodeDto(e.name(), e.getDescription()))
                .collect(Collectors.toList());
    }

    public static boolean isValidCode(String code) {
        return Arrays.stream(UserStatus.values())
                .anyMatch(status -> status.name().equals(code));
    }
}
