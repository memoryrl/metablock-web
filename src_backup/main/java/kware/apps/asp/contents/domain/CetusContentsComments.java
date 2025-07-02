package kware.apps.asp.contents.domain;

import cetus.bean.AuditBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CetusContentsComments extends AuditBean {
    /** 댓글 고유번호 */
    private Long uid;
    
    /** 컨텐츠 고유번호 */
    private Long contentsUid;
    
    /** 댓글 타입 */
    private String type;
    
    /** 평점 */
    private Integer ratings;
    
    /** 댓글 내용 */
    private String comment;
    
    /** 등록일시 */
    private String regDt;
    
    /** 수정일시 */
    private String updtDt;

}
