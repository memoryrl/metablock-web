package kware.apps.manager.cetus.form.domain;

import cetus.annotation.DisplayName;
import cetus.bean.AuditBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusColumnOptions extends AuditBean {
    private Long uid;
    private Long columnsUid;
    @NotBlank @DisplayName("라벨")
    private String label;
    @NotBlank @DisplayName("이름")
    private String name;
    private Integer sortNum;

    public void addUid(Long uid) {
        this.uid = uid;
    }

    public void addSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public void addColumnsUid(Long columnsUid) {
        this.columnsUid = columnsUid;
    }

    public void addAuthor(Long regUid) {
        setRegUid(regUid);
        setUpdtUid(regUid);
    }
}
