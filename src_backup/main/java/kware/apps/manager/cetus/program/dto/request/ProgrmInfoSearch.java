package kware.apps.manager.cetus.program.dto.request;


import lombok.Getter;

@Getter
public class ProgrmInfoSearch {

    private Long workplaceUid;
    private String useAt;
    private String browseText;

    public ProgrmInfoSearch(Long workplaceUid, String useAt, String browseText) {
        this.workplaceUid = workplaceUid;
        this.useAt = useAt;
        this.browseText = browseText;
    }

    public void setWorkplaceUid(Long workplaceUid) {
        this.workplaceUid = workplaceUid;
    }
}
