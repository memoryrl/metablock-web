package kware.apps.manager.cetus.form.dto.request;

import cetus.user.UserUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ColumnsOrder {
    @NotEmpty
    private List<Long> reorderedUid = new ArrayList<>();
    @NotBlank
    private String formGroup;

    private Long workplaceUid = UserUtil.getUser().getWorkplaceUid();
}
