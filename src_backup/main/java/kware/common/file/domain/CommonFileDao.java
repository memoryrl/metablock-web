package kware.common.file.domain;

import cetus.dao.CetusDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonFileDao extends CetusDaoSupport {
    
    public CommonFileDao() {
        super("file");
    }
    
    public Long key() {
        return selectOne("key");
    }

    public CommonFile view(CommonFile bean) {
        return selectOne("view", bean);
    }

    public List<CommonFile> list(CommonFile bean) {
        return selectList("list", bean);
    }

    public int delete(CommonFile bean) {
        return delete("delete", bean);
    }

    public int insert(CommonFile bean) {
        return insert("insert", bean);
    }

    public int insertLog(CommonFileLog bean) {
        return insert("insertLog", bean);
    }

    public int increaseDownCnt(CommonFile bean){return update("increaseDownCnt", bean);}
    
    public List<CommonFileLog> selectLog(CommonFileLog bean) {
        return selectList("selectLog", bean);
    }

	public CommonFile findFileInfo(CommonFile bean){
	    return selectOne("findFileInfo", bean);
	}
}
