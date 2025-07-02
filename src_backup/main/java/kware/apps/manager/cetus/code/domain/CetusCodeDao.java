package kware.apps.manager.cetus.code.domain;

import cetus.bean.Page;
import cetus.bean.Pageable;
import cetus.dao.SuperDao;
import kware.apps.manager.cetus.code.dto.request.CodePageSearch;
import kware.apps.manager.cetus.code.dto.response.CodePage;
import kware.apps.manager.cetus.code.dto.response.CodeView;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CetusCodeDao extends SuperDao<CetusCode> {

	public CetusCodeDao() {
		super("cetusCode");
	}

    public Page<CodePage> findPagingAll(CodePageSearch bean, Pageable pageable) {
        return page("findPagingAll", "findAllCount", bean, pageable);
    }

    public List<CodeView> findByUpperCode(String upperCode) {
        return selectList("findByUpperCode", upperCode);
    }

    public List<CodeView> findByCodeAndUseAt (String code, String useAt) {
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("code", code);
            put("useAt", useAt);
        }};
        return selectList("findByAllChildren", params);
    }

    public Optional<CetusCode> findByUid(Long uid) {
        return Optional.ofNullable(selectOne("findByUid", uid));
    }

    public Optional<Integer> isExistByCode(String code) {
        return Optional.ofNullable(selectOne("isExistByCode", code));
    }

    public int updateCodeUseAt(CetusCode bean) {
        return update("updateCodeUseAt", bean);
    }
}
