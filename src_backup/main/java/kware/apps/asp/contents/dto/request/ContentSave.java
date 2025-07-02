package kware.apps.asp.contents.dto.request;

import cetus.annotation.DisplayName;
import cetus.annotation.YOrN;
import kware.common.file.domain.CommonFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentSave {

    @NotBlank @DisplayName("프로그램명")
    private String progrmNm;

    @NotBlank @DisplayName("URL")
    private String url;

    @YOrN
    private String useAt;

    private String progrmDc;

    private List<CommonFile> leftImg;
    private List<CommonFile> rightImg;
    private List<CommonFile> logoImg;
    private List<CommonFile> companyImg;
    
    private String title1;
    private String title2;
    private String title3;

    @YOrN
    private String isRootUrl;

    public ContentSave(String progrmNm, String url, String useAt) {
        this.progrmNm = progrmNm;
        this.url = url;
        this.useAt = useAt;
    }
}
