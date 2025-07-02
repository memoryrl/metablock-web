package kware.apps.manager.cetus.form.domain;

import cetus.dao.SuperDao;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CetusColumnOptionsDao extends SuperDao<CetusColumnOptions> {

    public CetusColumnOptionsDao() {
        super("cetusColumnOptions");
    }

    public List<CetusColumnOptions> list(Long columnsUid) {
        return selectList("list", columnsUid);
    }

    public Optional<CetusColumnOptions> findByUid(Long uid) {
        return Optional.of(selectOne("findByUid", uid));
    }

    public Integer findNextSortNum(Long columnsUid) {
        return selectOne("findNextSortNum", columnsUid);
    }

    public void updateOrder(Long columnsUid, String name, Integer sortNum) {
        Map<String, Object> param = new HashMap<>();
        param.put("columnsUid", columnsUid);
        param.put("name", name);
        param.put("sortNum", sortNum);
        update("updateOrder", param);
    }

}
