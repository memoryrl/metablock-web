package kware.apps.manager.cetus.form.domain;

import cetus.dao.SuperDao;
import kware.apps.manager.cetus.form.dto.request.ColumnsOrder;
import kware.apps.manager.cetus.form.dto.request.ColumnsSearch;
import kware.apps.manager.cetus.form.dto.response.ColumnsPage;
import kware.apps.manager.cetus.form.dto.response.ColumnsView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CetusFormColumnsDao extends SuperDao<CetusFormColumns> {

    public CetusFormColumnsDao() {
        super("cetusFormColumns");
    }

    public List<ColumnsPage> page(ColumnsSearch bean) {
        return selectList("list",  bean);
    }

    public Optional<ColumnsView> findByUid(Long uid) {
        return Optional.of(selectOne("findByUid", uid));
    }

    public Integer findNextSortNum(@Param("formGroup") String formGroup) {
        return selectOne("findNextSortNum", formGroup);
    }

    public List<ColumnsPage> findByTenanyAndFormGroup(ColumnsSearch bean) {
        return selectList("findByTenanyAndFormGroup", bean);
    }

    public void deleteColumns(@Param("uid") Long uid) {
        update("deleteColumns", uid);
    }


    public Integer existFieldName(String formGroup, String fieldName, Long workplaceUid) {
        Map<String, Object> param = new HashMap<>();
        param.put("formGroup", formGroup);
        param.put("name", fieldName);
        param.put("workplaceUid", workplaceUid);

        return selectOne("existFieldName", param);
    }

    public void updateOrder(Long uid, Integer sortNum) {
        Map<String, Object> param = new HashMap<>();
        param.put("uid", uid);
        param.put("sortNum", sortNum);
        update("updateOrder", param);
    }

    public Optional<ColumnsView> selectNextByFormGroup(ColumnsOrder bean) {
        return Optional.ofNullable(selectOne("selectNextByFormGroup", bean));
    }

    public Optional<ColumnsView> selectPrevByFormGroup(ColumnsOrder bean) {
        return Optional.ofNullable(selectOne("selectPrevByFormGroup", bean));
    }

}


