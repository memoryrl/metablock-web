package cetus.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import kware.apps.manager.cetus.user.domain.CetusUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor  // 파라미터가 없는 기본 생성자
@AllArgsConstructor // 모든 필드 값을 파라미터로 받음
public class CetusBean {

    private Long thumbnailFileUid;      // 썸네일 파일 UID
    public void setDataWithUser(CetusUser user) { }
}
