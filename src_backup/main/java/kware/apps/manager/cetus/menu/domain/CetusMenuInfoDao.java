package kware.apps.manager.cetus.menu.domain;

import cetus.dao.SuperDao;
import kware.apps.manager.cetus.menu.dto.request.MenuRootSearch;
import kware.apps.manager.cetus.menu.dto.request.MenuTreeSearch;
import kware.apps.manager.cetus.menu.dto.request.SessionMenuListSearch;
import kware.apps.manager.cetus.menu.dto.response.MenuTreeList;
import kware.apps.manager.cetus.menu.dto.response.SessionMenuList;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CetusMenuInfoDao extends SuperDao<CetusMenuInfo> {

	public CetusMenuInfoDao() {
		super("cetusMenuInfo");
	}

	public List<SessionMenuList> findSessionMenuList(SessionMenuListSearch search) {
		return selectList("findSessionMenuList", search);
	}

	public List<MenuTreeList> findMenuTreeList(MenuTreeSearch search) {
		return selectList("findMenuTreeList", search);
	}

	public Long getRootMenuNo(MenuRootSearch search) {
		return selectOne("getRootMenuNo", search);
	}

	public List<CetusMenuInfo> getMenuByProgramUid(Long programUid) {
		return selectList("getMenuByProgramUid", programUid);
	}

	public SessionMenuList getRootMenuInfo(Long rootMenuNo) {
		return selectOne("getRootMenuInfo", rootMenuNo);
	}

}
