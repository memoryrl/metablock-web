package kware.apps.manager.cetus.dept.dto.response;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeptTreeList {

    private Long no;
    private Long upperNo;
    private String text;
    private String treeDeptNm;
    private String regDt;
    private String regNm;
    private String updtDt;
    private String updtNm;
    private Integer depth;
    private String deptPath;
    private String isLeaf;

}
