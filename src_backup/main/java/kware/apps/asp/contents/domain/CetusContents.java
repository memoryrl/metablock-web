package kware.apps.asp.contents.domain;

import cetus.bean.AuditBean;
import kware.apps.asp.contents.dto.request.ContentChange;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusContents extends AuditBean {

    private Long uid;
    private String title;
    private String description;
    private String contents;
    private String metadata; 
    private String ratings;
    private String sampleData;
    private Long fileUid;
    private Long thumbnailUid;
    private String regDt;
    private String updtDt;

    public void setFileUids(Long fileUid, Long thumbnailUid) {
        this.fileUid = fileUid;
        this.thumbnailUid = thumbnailUid;
    }

    public CetusContents changeContent(Long uid, ContentChange request) {
        this.uid = uid;
        this.title = (request.getTitle() != null) ? request.getTitle() : this.title;
        this.description = (request.getDescription() != null) ? request.getDescription() : this.description;
        this.contents = (request.getContents() != null) ? request.getContents() : this.contents;
        this.metadata = (request.getMetadata() != null) ? request.getMetadata() : this.metadata;
        this.sampleData = (request.getSampleData() != null) ? request.getSampleData() : this.sampleData;
        return this;
    }

}
