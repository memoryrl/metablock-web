package kware.apps.manager.cetus.form.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionsOrder {
    @NotEmpty
    private List<String> nameList = new ArrayList<>();
    @NotNull
    private Long columnsUid;
}
