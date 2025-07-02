package kware.apps.manager.cetus.invite.domain;

import cetus.bean.AuditBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CetusInvite extends AuditBean {

    private Long uid;
    private String email;
    private String url;
    private LocalDateTime expirationDate;
    private String useAt;

}
