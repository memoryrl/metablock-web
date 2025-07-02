package kware.apps.manager.cetus.program.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class ProgrmFullInfoSearch {

    private String url;
    private Long workplaceUid;

    public ProgrmFullInfoSearch(String url, Long workplaceUid) {
        this.url = url;
        this.workplaceUid = workplaceUid;
    }
}
