package kware.apps.manager.cetus.bbsctt.domain;

import cetus.bean.AuditBean;
import kware.apps.manager.cetus.bbsctt.dto.request.BbscttChange;
import kware.apps.manager.cetus.bbsctt.dto.request.BbscttSave;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusBbsctt extends AuditBean {

    private Long bbscttUid;
    private Long bbsUid;
    private Long clUid;
    private String bbscttNm;
    private int rdCnt;
    private String bbscttCnt;
    private String noticeAt;
    private Long fileUid;
    private String useAt;
    private String openAt;
    private String deleteAt;
    private String thumbnailSrc;

    public CetusBbsctt(BbscttSave request, Long fileUid) {
        this.bbsUid = request.getBbsUid();
        this.bbscttNm = request.getBbscttNm();
        this.bbscttCnt = request.getBbscttCnt();
        this.openAt = request.getOpenAt();
        this.noticeAt = request.getNoticeAt();
        this.thumbnailSrc = request.getThumbnailSrc();
        this.fileUid = fileUid;
    }

    public CetusBbsctt changeBbsctt(Long bbscttUid, BbscttChange request, Long fileUid) {
        this.bbscttUid = bbscttUid;
        this.bbscttNm = (request.getBbscttNm() != null) ? request.getBbscttNm() : this.bbscttNm;
        this.bbscttCnt = (request.getBbscttCnt() != null) ? request.getBbscttCnt() : this.bbscttCnt;
        this.openAt = (request.getOpenAt() != null) ? request.getOpenAt() : this.openAt;
        this.noticeAt = (request.getNoticeAt() != null) ? request.getNoticeAt() : this.noticeAt;
        this.thumbnailSrc = (request.getThumbnailSrc() != null) ? request.getThumbnailSrc() : null;
        this.fileUid = fileUid;
        return this;
    }

    public CetusBbsctt(Long bbscttUid, String openAt) {
        this.bbscttUid = bbscttUid;
        this.openAt = openAt;
    }
}
