package cetus.bean;

import kware.common.file.domain.CommonFile;
import lombok.Getter;


@Getter
public class FileBean {

    private CommonFile[] fileAdd;
    private CommonFile[] fileDel;
}
