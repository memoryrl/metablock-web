package kware.apps.manager.cetus.form.domain;

import cetus.bean.AuditBean;
import kware.apps.manager.cetus.form.dto.request.ColumnsChange;
import kware.apps.manager.cetus.form.dto.request.ColumnsSave;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CetusFormColumns extends AuditBean {
    private Long uid;
    private String type;
    private String label;
    private boolean required;
    private String description;
    private String useAt;
    private String name;
    private String placeholder;
    private String formGroup;
    private Integer sortNum;
    private String defaultValue;


    public CetusFormColumns(ColumnsSave request) {
        this.type = request.getType();
        this.label = request.getLabel();
        this.required = request.isRequired();
        this.description = request.getDescription();
        this.useAt = request.getUseAt();
        this.name = request.getName();
        this.placeholder = request.getPlaceholder();
        this.formGroup =request.getFormGroup();
        this.sortNum = request.getSortNum();
        this.defaultValue = request.getDefaultValue();
    }

    public CetusFormColumns changeColumns(Long uid, ColumnsChange request) {
        this.uid = uid;
        this.type = request.getType() != null ? request.getType() : this.type;
        this.label = request.getLabel() != null ? request.getLabel() : this.label;
        this.required = request.isRequired();
        this.description = request.getDescription() != null ? request.getDescription() : this.description;
        this.useAt = request.getUseAt() != null ? request.getUseAt() : this.useAt;
        this.name = request.getName() != null ? request.getName() : this.name;
        this.placeholder = request.getPlaceholder() != null ? request.getPlaceholder() : this.placeholder;
        this.sortNum = request.getSortNum() != null ? request.getSortNum() : this.sortNum;
        this.defaultValue = request.getDefaultValue() != null ? request.getDefaultValue() : this.defaultValue;
        return this;
    }
}
