package cetus.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * Mapper namespace 반복 없도록 한다.
 */
public class CetusDaoSupport extends EgovAbstractMapper {
    
    private final String queryIdPrefix;
    
    public CetusDaoSupport(String queryIdPrefix) {
        this.queryIdPrefix = queryIdPrefix + ".";
    }

    // 전자정부 재정의. 네임스페이스 추가

    @Override
    public <E> List<E> selectList(String queryId) {
        return super.selectList(queryIdPrefix + queryId);
    }

    @Override
    public <E> List<E> selectList(String queryId, Object bean) {
        return super.selectList(queryIdPrefix + queryId, bean);
    }

    @Override
    public <E> List<E> selectList(String queryId, Object bean, RowBounds rowBounds) {
        return super.selectList(queryIdPrefix + queryId, bean, rowBounds);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String queryId, Object bean, String mapKey) {
        return super.selectMap(queryIdPrefix + queryId, bean, mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String queryId, Object bean, String mapKey, RowBounds rowBounds) {
        return super.selectMap(queryIdPrefix + queryId, bean, mapKey, rowBounds);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String queryId, String mapKey) {
        return super.selectMap(queryIdPrefix + queryId, mapKey);
    }

    @Override
    public <T> T selectOne(String queryId) {
        return super.selectOne(queryIdPrefix + queryId);
    }

    @Override
    public <T> T selectOne(String queryId, Object bean) {
        return super.selectOne(queryIdPrefix + queryId, bean);
    }

    @Override
    public int insert(String queryId) {
        return super.insert(queryIdPrefix + queryId);
    }

    @Override
    public int insert(String queryId, Object bean) {
        return super.insert(queryIdPrefix + queryId, bean);
    }

    @Override
    public int update(String queryId) {
        return super.update(queryIdPrefix + queryId);
    }

    @Override
    public int update(String queryId, Object bean) {
        return super.update(queryIdPrefix + queryId, bean);
    }

    @Override
    public int delete(String queryId) {
        return super.delete(queryIdPrefix + queryId);
    }
    @Override
    public int delete(String queryId, Object bean) {
        return super.delete(queryIdPrefix + queryId, bean);
    }

    @Override
    public List<?> listWithPaging(String queryId, Object bean, int pageIndex, int pageSize) {
        return super.listWithPaging(queryIdPrefix + queryId, bean, pageIndex, pageSize);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void listToOutUsingResultHandler(String queryId, ResultHandler handler) {
        super.listToOutUsingResultHandler(queryIdPrefix + queryId, handler);
    }
    
    //전자정부 프레임워크에서는 변수를 받는 함수가 없어서 신규로 생성함 by kljang 20230323
    @SuppressWarnings("rawtypes")
    public void listToOutUsingResultHandler(String queryId, Object bean, ResultHandler handler) {
    	this.getSqlSession().select(queryIdPrefix + queryId, bean, handler);
    }

    /* pure method */
    public <E> List<E> selectListPure(String queryId) {
        return super.selectList(queryId);
    }

    public <E> List<E> selectListPure(String queryId, Object bean) {
        return super.selectList(queryId, bean);
    }

    public <E> List<E> selectListPure(String queryId, Object bean, RowBounds rowBounds) {
        return super.selectList(queryId, bean, rowBounds);
    }

    public <K, V> Map<K, V> selectMapPure(String queryId, Object bean, String mapKey) {
        return super.selectMap(queryId, bean, mapKey);
    }

    public <K, V> Map<K, V> selectMapPure(String queryId, Object bean, String mapKey, RowBounds rowBounds) {
        return super.selectMap(queryId, bean, mapKey, rowBounds);
    }

    public <K, V> Map<K, V> selectMapPure(String queryId, String mapKey) {
        return super.selectMap(queryId, mapKey);
    }

    public <T> T selectOnePure(String queryId) {
        return super.selectOne(queryId);
    }

    public <T> T selectOnePure(String queryId, Object bean) {
        return super.selectOne(queryId, bean);
    }

    public int insertPure(String queryId) {
        return super.insert(queryId);
    }

    public int insertPure(String queryId, Object bean) {
        return super.insert(queryId, bean);
    }

    public int updatePure(String queryId) {
        return super.update(queryId);
    }

    public int updatePure(String queryId, Object bean) {
        return super.update(queryId, bean);
    }

    public int deletePure(String queryId) {
        return super.delete(queryId);
    }
    public int deletePure(String queryId, Object bean) {
        return super.delete(queryId, bean);
    }

    public List<?> listWithPagingPure(String queryId, Object bean, int pageIndex, int pageSize) {
        return super.listWithPaging(queryId, bean, pageIndex, pageSize);
    }

    @SuppressWarnings("rawtypes")
    public void listToOutUsingResultHandlerPure(String queryId, ResultHandler handler) {
        super.listToOutUsingResultHandler(queryId, handler);
    }

}