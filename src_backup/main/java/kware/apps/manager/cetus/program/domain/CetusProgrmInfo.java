package kware.apps.manager.cetus.program.domain;


import cetus.bean.AuditBean;
import kware.apps.manager.cetus.program.dto.request.ProgramChange;
import kware.apps.manager.cetus.program.dto.request.ProgramSave;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusProgrmInfo extends AuditBean {

    private Long uid;
    private String progrmNm;
    private String url;
    private String progrmDc;
    private String useAt;
    private Long workplaceUid;

    private Long leftSlideImg;
    private Long rightSlideImg;
    private Long logoImg;
    private Long companyLogoImg;
    private String title1;
    private String title2;
    private String title3;
    private String isRootUrl;

    public CetusProgrmInfo(ProgramSave request, Long workplaceUid) {
        this.progrmNm = request.getProgrmNm();
        this.url = request.getUrl();
        this.progrmDc = request.getProgrmDc();
        this.useAt = request.getUseAt();
        this.workplaceUid = workplaceUid;
        this.title1 = request.getTitle1();
        this.title2 = request.getTitle2();
        this.title3 = request.getTitle3();
        this.isRootUrl = request.getIsRootUrl();
    }

    public void setImgUids(Long leftSlideImg, Long rightSlideImg, Long logoImg, Long companyLogoImg) {
        this.leftSlideImg = leftSlideImg;
        this.rightSlideImg = rightSlideImg;
        this.logoImg = logoImg;
        this.companyLogoImg = companyLogoImg;
    }

    public CetusProgrmInfo changeProgram(Long uid, ProgramChange request) {
        this.uid = uid;
        this.progrmNm = (request.getProgrmNm() != null) ? request.getProgrmNm() : this.progrmNm;
        this.url = (request.getUrl() != null) ? request.getUrl() : this.url;
        this.progrmDc = (request.getProgrmDc() != null) ? request.getProgrmDc() : this.progrmDc;
        this.useAt = (request.getUseAt() != null) ? request.getUseAt() : this.useAt;
        this.title1 = (request.getTitle1() != null) ? request.getTitle1() : this.title1;
        this.title2 = (request.getTitle2() != null) ? request.getTitle2() : this.title2;
        this.title3 = (request.getTitle3() != null) ? request.getTitle3() : this.title3;
        this.isRootUrl = (request.getIsRootUrl() != null) ? request.getIsRootUrl() : this.isRootUrl;
        return this;
    }
}
