package kware.apps.manager.cetus.statushist.dto.request;


import lombok.Getter;

@Getter
public class UserStatusHistExcelSearch {

    private Long userUid;

    public UserStatusHistExcelSearch(Long userUid) {
        this.userUid = userUid;
    }
}
