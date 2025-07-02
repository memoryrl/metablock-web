package kware.apps.manager.cetus.bbsctt.dto.request;


import lombok.Getter;

@Getter
public class BbscttSearch {

    private Long bbsUid;
    private Long regUid;
    private String openAt;
    private String orderByNotice;

    public BbscttSearch(Long bbsUid, Long regUid, String openAt, String orderByNotice) {
        this.bbsUid = bbsUid;
        this.regUid = regUid;
        this.openAt = openAt;
        this.orderByNotice = orderByNotice;
    }
}
