package kware.apps.manager.cetus.position.domain;

import cetus.bean.AuditBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusPosition extends AuditBean {

    private Long uid;
    private String name;
    private String description;
    private Long sortOrder;
    private String useAt;
}
