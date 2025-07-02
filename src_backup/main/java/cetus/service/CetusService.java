package cetus.service;

import cetus.annotation.Key;
import cetus.bean.CetusBean;
import cetus.bean.Page;
import cetus.bean.Pageable;
import cetus.dao.CetusDao;
import cetus.support.BeanValidator;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CetusService<B extends CetusBean, D extends CetusDao<B>> extends EgovAbstractServiceImpl {
    
    protected D dao;
    
    @Autowired
    public void setDao(D dao) {
        this.dao = dao;
    }
    
    /**
     * 전체 목록 조회
     * @return
     */
    public List<B> list() {
        return dao.list(null);
    }
    
    /**
     * 목록 조회
     * @param bean
     * @return
     */
    public List<B> list(B bean) {
        return dao.list(bean);
    }
    
    /**
     * 목록 조회(페이징)
     * @param bean
     * @param pageable
     * @return
     */
    public Page<B> page(B bean, Pageable pageable) {
        return dao.page(bean, pageable);
    }
    
    /**
     * count(*)
     * @param bean
     * @return
     */
    public int count(B bean) {
        return dao.count(bean);
    }
    
    /**
     * 단일건 조회
     * @param bean
     * @return
     */
    public B view(B bean) {
        return dao.view(bean);
    }
    
    /**
     * 등록
     * @param bean
     * @return
     */
    @Transactional
    public int insert(B bean) {
        BeanValidator.validate(bean);
        return dao.insert(bean);
    }
    
    /**
     * 수정
     * @param bean
     * @return
     */
    @Transactional
    public int update(B bean) {
        BeanValidator.validate(bean);
        return dao.update(bean);
    }
    
    /**
     * 삭제
     * @param bean
     * @return
     */
    @Transactional
    public int delete(B bean) {
        BeanValidator.validate(bean, Key.class);
        return dao.delete(bean);
    }
}