package kware.apps.manager.cetus.program.dto.request;


import lombok.Getter;

@Getter
public class MenuProgrmInfoSearch {

    private String useAt;
    private Long workplaceUid;
    private String browseText;

    public MenuProgrmInfoSearch(String useAt, Long workplaceUid, String browseText) {
        this.useAt = useAt;
        this.workplaceUid = workplaceUid;
        this.browseText = browseText;
    }

    public void setWorkplaceUid(Long workplaceUid) {
        this.workplaceUid = workplaceUid;
    }
}
