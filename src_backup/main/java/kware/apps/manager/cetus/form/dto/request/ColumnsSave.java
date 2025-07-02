package kware.apps.manager.cetus.form.dto.request;

import cetus.annotation.DisplayName;
import kware.apps.manager.cetus.form.domain.CetusColumnOptions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ColumnsSave {
    @NotBlank @DisplayName("타입")
    private String type;
    @NotBlank @DisplayName("라벨")
    private String label;
    @NotNull @DisplayName("필수")
    private boolean required;
    private String description;
    private String useAt;
    @NotBlank @DisplayName("이름")
    private String name;
    @NotBlank @DisplayName("placeholder")
    private String placeholder;
    @NotBlank @DisplayName("폼 그룹")
    private String formGroup;
    private Integer sortNum;
    private String defaultValue;
    private List<CetusColumnOptions> options = new ArrayList<>();

    public void addOrder(Integer sortNum) {
        this.sortNum = sortNum;
    }
}
