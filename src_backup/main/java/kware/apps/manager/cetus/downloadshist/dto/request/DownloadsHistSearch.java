package kware.apps.manager.cetus.downloadshist.dto.request;

import lombok.Getter;

@Getter
public class DownloadsHistSearch {

    Long userUid;

    public DownloadsHistSearch(Long userUid) {
        this.userUid = userUid;
    }

    public void setUserUid(Long userUid) {
        this.userUid = userUid;
    }
}
