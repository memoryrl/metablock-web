package cetus.dao;

import cetus.bean.CetusBean;
import cetus.bean.Page;
import cetus.bean.Pageable;

import java.util.List;

public class CetusDao<B extends CetusBean> extends CetusDaoSupport {

    public CetusDao(String queryIdPrefix) {
        super(queryIdPrefix);
    }

    /**
     * 생성자
     */
    public List<B> list(B bean) {
        return selectList("list", bean);
    }

    /**
     * 데이터 건수 조회
     */
    public int count(B bean) {
        return selectOne("count", bean);
    }

    /**
     * 목록 조회(페이징)
     * @param bean
     * @param pageable
     * @return
     */
    public Page<B> page(B bean, Pageable pageable) {
        List<B> list = selectList("page", pageable.generateMap(bean));
        int count = count(bean);
        return new Page<>(list, count, pageable);
    }

    /**
     * 목록 조회(페이징)
     * @param bean
     * @param pageable
     * @param listQueryId
     * @param countQueryId
     * @return
     */
    public Page<B> page(B bean, Pageable pageable, String listQueryId, String countQueryId) {
        List<B> list = selectList(listQueryId, pageable.generateMap(bean));
        int count = selectOne(countQueryId, bean);
        return new Page<>(list, count, pageable);
    }

    /**
     * 단일건 조회
     * @param bean
     * @return
     */
    public B view(B bean) {
        return selectOne("view", bean);
    }

    /**
     * 등록
     * @param bean
     * @return
     */
    public int insert(B bean) {
        return insert("insert", bean);
    }

    /**
     * 수정
     * @param bean
     * @return
     */
    public int update(B bean) {
        return update("update", bean);
    }

    /**
     * 삭제
     * @param bean
     * @return
     */
    public int delete(B bean) {
        return delete("delete", bean);
    }


}
