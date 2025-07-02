package kware.apps.manager.cetus.bbsctt.domain;

import cetus.bean.Page;
import cetus.bean.Pageable;
import cetus.dao.SuperDao;
import kware.apps.manager.cetus.bbsctt.dto.request.BbscttExcelSearch;
import kware.apps.manager.cetus.bbsctt.dto.request.BbscttRecentSearch;
import kware.apps.manager.cetus.bbsctt.dto.response.BbscttExcelList;
import kware.apps.manager.cetus.bbsctt.dto.response.BbscttList;
import kware.apps.manager.cetus.bbsctt.dto.response.BbscttRecentList;
import kware.apps.manager.cetus.bbsctt.dto.response.BbscttView;
import kware.apps.manager.cetus.loginhist.dto.request.UserLoginHistExcelSearch;
import kware.apps.manager.cetus.loginhist.dto.response.UserLoginHistExcelList;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CetusBbscttDao extends SuperDao<CetusBbsctt> {

    public CetusBbscttDao() {
        super("cetusBbsctt");
    }

    public int increaseViewCount(Long bbscttUid) {
        return update("increaseViewCount", bbscttUid);
    }

    public BbscttView getViewByBbscttUid(Long bbscttUid) {
        return selectOne("getViewByBbscttUid", bbscttUid);
    }

    public int updateBbscttOpenAt(CetusBbsctt bean) {
        return update("updateBbscttOpenAt", bean);
    }

    public Page<BbscttExcelList> bbscttExcelPage(BbscttExcelSearch search, Pageable pageable) {
        return page("bbscttExcelPageList", "bbscttExcelPageListCount", search, pageable);
    }

    public List<BbscttRecentList> getRecentBbsctt(BbscttRecentSearch search) {
        return selectList("getRecentBbsctt", search);
    }
}
