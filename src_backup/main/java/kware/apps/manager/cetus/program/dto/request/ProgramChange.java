package kware.apps.manager.cetus.program.dto.request;

import cetus.annotation.DisplayName;
import cetus.annotation.YOrN;
import kware.common.file.domain.CommonFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProgramChange {

    @NotBlank @DisplayName("프로그램명")
    private String progrmNm;

    @NotBlank @DisplayName("URL")
    private String url;

    @YOrN
    private String useAt;

    private String progrmDc;

    private List<CommonFile> leftImg;
    private List<CommonFile> leftImgDel;
    private Long leftImgUid;

    private List<CommonFile> rightImg;
    private List<CommonFile> rightImgDel;
    private Long rightImgUid;

    private List<CommonFile> logoImg;
    private List<CommonFile> logoImgDel;
    private Long logoImgUid;

    private List<CommonFile> companyImg;
    private List<CommonFile> companyImgDel;
    private Long companyImgUid;

    private String title1;
    private String title2;
    private String title3;

    @YOrN
    private String isRootUrl;

    public ProgramChange(String progrmNm, String useAt) {
        this.progrmNm = progrmNm;
        this.useAt = useAt;
    }
}
