package kware.apps.manager.cetus.code.dto.request;

import cetus.annotation.YOrN;
import lombok.Getter;

@Getter
public class CodePageSearch {

    private String browseText;

    @YOrN
    private String useAt;

    public CodePageSearch(String browseText, String useAt) {
        this.browseText = browseText;
        this.useAt = useAt;
    }
}
