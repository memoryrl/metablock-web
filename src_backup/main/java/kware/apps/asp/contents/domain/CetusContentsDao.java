package kware.apps.asp.contents.domain;


import java.util.List;

import kware.apps.asp.contents.dto.request.CommentsSearch;
import org.springframework.stereotype.Component;

import cetus.dao.SuperDao;
import kware.apps.asp.contents.dto.response.ContentsView;

@Component
public class CetusContentsDao extends SuperDao<CetusContents> {

    public CetusContentsDao() {
        super("cetusContents");
    }

    public ContentsView contentsView(Long uid) {
        return selectOne("view", uid);
    }

    public void contentsUpdate(ContentsView bean) {
        update("contentsUpdate", bean);
    }

    public List<CetusTags> findTagsByContentsUid(Long uid) {
        return selectList("findTagsByContentsUid", uid);
    }

    public void insertComment(CetusContentsComment comment) {
        insert("insertComment", comment);
    }

    public List<CetusContentsComment> listComments(Long contentsUid) {
        return selectList("listComments", contentsUid);
    }

    public Integer commentRatingAvg(CommentsSearch search) {
        return selectOne("commentRatingAvg", search);
    }

    public Integer findCntByType(CommentsSearch search) {
        return selectOne("findCntByType", search);
    }
}
