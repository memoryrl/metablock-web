package kware.apps.asp.bookmark;

import cetus.bean.CetusBean;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
public class CetusBookmark extends CetusBean {
    private Long userUid;
    @NotNull
    private Long contentsUid;
    private Long regUid;
    private String regDt;
    private Long updtUid;
    private String updtDt;

    public CetusBookmark(Long userUid, Long contentsUid) {
        this.userUid = userUid;
        this.contentsUid = contentsUid;
    }

    private String title;
    private String description;
    private Long fileUid;
    private String fileId;
}
