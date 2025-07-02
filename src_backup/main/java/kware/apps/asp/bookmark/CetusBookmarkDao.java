package kware.apps.asp.bookmark;

import cetus.dao.CetusDao;
import kware.apps.asp.bookmark.dto.request.CetusSearchBookmark;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CetusBookmarkDao extends CetusDao<CetusBookmark> {

    public CetusBookmarkDao(){
        super("cetusBookmark");
    }

    public List<CetusBookmark> findListByUserUid(CetusSearchBookmark bean){
        return selectList("findListByUserUid", bean);
    }

    public Boolean isBookmarkExists(CetusBookmark bean){
        return selectOne("isBookmarkExists", bean);
    }
}
