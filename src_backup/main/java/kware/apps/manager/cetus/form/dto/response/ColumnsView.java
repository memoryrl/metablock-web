package kware.apps.manager.cetus.form.dto.response;

import kware.apps.manager.cetus.form.domain.CetusColumnOptions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ColumnsView {

    private Long uid;
    private Long workplaceUid;
    private String type;
    private String label;
    private boolean required;
    private String description;
    private String useAt;
    private String name;
    private String placeholder;
    private Long regUid;
    private String userNm;
    private Long updtUid;
    private String regDt;
    private String updtDt;
    private String formGroup;
    private Integer sortNum;
    private String defaultValue;

    private List<CetusColumnOptions> options = new ArrayList<>();


    public void addOptions(List<CetusColumnOptions> options) {
        this.options.addAll(options);
    }
}
