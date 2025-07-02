package kware.apps.manager.cetus.filelog.dto.request;

import lombok.Getter;

@Getter
public class FileLogExcelSearch {

    private Long userUid;

    public FileLogExcelSearch(Long userUid) {
        this.userUid = userUid;
    }
}
