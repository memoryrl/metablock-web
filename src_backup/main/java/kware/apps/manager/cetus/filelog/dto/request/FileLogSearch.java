package kware.apps.manager.cetus.filelog.dto.request;

import lombok.Getter;

@Getter
public class FileLogSearch {

    private Long userUid;

    public FileLogSearch(Long userUid) {
        this.userUid = userUid;
    }
}
