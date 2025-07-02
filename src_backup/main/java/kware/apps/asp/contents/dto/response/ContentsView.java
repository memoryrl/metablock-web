package kware.apps.asp.contents.dto.response;

import kware.apps.asp.contents.domain.CetusTags;
import kware.apps.asp.contents.dto.request.ContentChange;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentsView {

    private Long uid;
    private Long workplaceUid;
    private String title;
    private String description;
    private String contents;
    private String metadata;
    private Integer ratings;
    private String bookmark;
    private String sampleData;
    private Long fileUid;
    private String fileId;
    private String filePath;
    private String fileType;
    private String fileName;
    private Long thumbnailUid;
    private String thumbnailId;
    private String regDt;
    private String updtDt;
    private Long regUid;
    private Long updtUid;

    private List<CetusTags> tags;


    public void setFileUids(Long fileUid, Long thumbnailUid) {
        this.fileUid = fileUid;
        this.thumbnailUid = thumbnailUid;
    }

    public ContentsView changeContent(Long uid, ContentChange request) {
        this.uid = uid;
        this.title = (request.getTitle() != null) ? request.getTitle() : this.title;
        this.description = (request.getDescription() != null) ? request.getDescription() : this.description;
        this.contents = (request.getContents() != null) ? request.getContents() : this.contents;
        this.metadata = (request.getMetadata() != null) ? request.getMetadata() : this.metadata;
        this.sampleData = (request.getSampleData() != null) ? request.getSampleData() : this.sampleData;
        return this;
    }
}
