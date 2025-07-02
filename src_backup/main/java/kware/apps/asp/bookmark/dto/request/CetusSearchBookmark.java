package kware.apps.asp.bookmark.dto.request;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CetusSearchBookmark {
    private Long userUid;
    @NotNull
    private Long contentsUid;
    // @NotBlank
    // private String targetType;
    private String regDt;
    private Long regUid;
    private String updtDt;
    private Long updtUid;


    public CetusSearchBookmark(Long userId) {
        this.userUid = userId;
    }
}
