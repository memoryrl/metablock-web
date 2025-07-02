package cetus.dao;

import cetus.bean.Page;
import cetus.bean.Pageable;
import kware.common.exception.SimpleException;

import java.util.List;

public class SuperDao<B> extends CetusDaoSupport {

    public SuperDao(String queryIdPrefix) {
        super(queryIdPrefix);
    }

    public <T, R> List<R> list(T bean) {
        return selectList("list", bean);
    }

    /**
     * 목록 조회(페이징)
     *
     * @param bean
     * @param pageable
     * @return
     */
    public <T, R> Page<R> page(String listQueryId, String countQueryId, T bean, Pageable pageable) {
        List<R> list = selectList(listQueryId, pageable.generateMap(bean));
        int count = selectOne(countQueryId, bean);
        return new Page<>(list, count, pageable);
    }

    /**
     * 단일건 조회 업데이트 용
     *
     * @param uid
     * @return
     */
    public B view(Long uid) {
        return selectOne("view", uid);
    }

    /**
     * 단일건 조회 업데이트 용
     * @param uid
     * @return
     */

    /**
     * 등록
     *
     * @param bean
     * @return
     */
    public int insert(B bean) {
        try {
            return insert("insert", bean);
        } catch (Exception e) {
            throw new SimpleException();
        }
    }

    /**
     * 수정
     *
     * @param bean
     * @return
     */
    public void update(B bean) {
        try {
            update("update", bean);
        } catch (Exception e) {
            throw new SimpleException();
        }
    }

    /**
     * 삭제
     *
     * @param uid
     * @return
     */
    public void delete(Long uid) {
        try {
            delete("delete", uid);
        } catch (Exception e) {
            throw new SimpleException();
        }
    }

}
