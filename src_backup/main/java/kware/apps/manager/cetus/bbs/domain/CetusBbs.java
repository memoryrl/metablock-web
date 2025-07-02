package kware.apps.manager.cetus.bbs.domain;

import cetus.bean.AuditBean;
import kware.apps.manager.cetus.bbs.dto.request.BbsChange;
import kware.apps.manager.cetus.bbs.dto.request.BbsSave;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusBbs extends AuditBean {

    private Long bbsUid;
    private String bbsNm;
    private String bbsTpCd;
    private String useAt;
    private String deleteAt;
    private String bbsClUseAt;
    private String atchAt;
    private Integer atchNum;
    private Integer uploadCpcty;
    private String answerUseAt;

    public CetusBbs(BbsSave request) {
        this.bbsNm = request.getBbsNm();
        this.bbsTpCd = request.getBbsTpCd();
        this.useAt = request.getUseAt();
        this.bbsClUseAt = request.getBbsClUseAt();
        this.atchAt = request.getAtchAt();
        this.atchNum = request.getAtchNum();
        this.uploadCpcty = request.getUploadCpcty();
        this.answerUseAt = request.getAnswerUseAt();
    }

    public CetusBbs changeBbs(Long uid, BbsChange request) {
        this.bbsUid = uid;
        this.bbsNm = (request.getBbsNm() != null) ? request.getBbsNm() : this.bbsNm;
        this.useAt = (request.getUseAt() != null) ? request.getUseAt() : this.useAt;
        this.bbsClUseAt = (request.getBbsClUseAt() != null) ? request.getBbsClUseAt() : this.bbsClUseAt;
        this.atchAt = (request.getAtchAt() != null) ? request.getAtchAt() : this.atchAt;
        this.atchNum = request.getAtchNum();
        this.uploadCpcty = request.getUploadCpcty();
        this.answerUseAt = (request.getAnswerUseAt() != null) ? request.getAnswerUseAt() : this.answerUseAt;
        return this;
    }
}
