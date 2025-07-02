package kware.common.config.session;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.session")
@Getter @Setter
public class SessionStore {
    private String storeType;
}
