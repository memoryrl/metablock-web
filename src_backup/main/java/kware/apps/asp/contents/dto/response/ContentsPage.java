package kware.apps.asp.contents.dto.response;

import cetus.bean.AuditBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentsPage extends AuditBean {

    private String uid;
    private String title;
    private String regDt;
}
