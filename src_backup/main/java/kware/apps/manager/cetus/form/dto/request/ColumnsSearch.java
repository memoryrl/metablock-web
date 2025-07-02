package kware.apps.manager.cetus.form.dto.request;

import cetus.user.UserUtil;
import lombok.Getter;
 @Getter
public class ColumnsSearch {
    private String type;
    private String formGroup;
    private Long workplaceUid;

     public ColumnsSearch(String type, String formGroup) {
         this.type = type;
         this.formGroup = formGroup;
         this.workplaceUid = UserUtil.getUserWorkplaceUid();
     }
 }
