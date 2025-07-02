package kware.apps.manager.cetus.answer.dto.request;

import lombok.Getter;

@Getter
public class AnswerExcelSearch {

    private Long bbsUid;
    private Long regUid;
    private String bbscttUrlPrefix;

    public AnswerExcelSearch(Long bbsUid, Long regUid, String bbscttUrlPrefix) {
        this.bbsUid = bbsUid;
        this.regUid = regUid;
        this.bbscttUrlPrefix = bbscttUrlPrefix;
    }

    public void setBaseUrl(String baseUrl) {
        this.bbscttUrlPrefix = baseUrl.replaceAll("/+$", "") + "/asp/cetus/bbsctt/view/";
    }
}
