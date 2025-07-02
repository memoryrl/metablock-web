package kware.apps.manager.cetus.form.service;

import cetus.user.UserUtil;
import kware.apps.manager.cetus.form.domain.CetusColumnOptions;
import kware.apps.manager.cetus.form.domain.CetusColumnOptionsDao;
import kware.apps.manager.cetus.form.domain.CetusFormColumns;
import kware.apps.manager.cetus.form.domain.CetusFormColumnsDao;
import kware.apps.manager.cetus.form.dto.request.*;
import kware.apps.manager.cetus.form.dto.response.ColumnsPage;
import kware.apps.manager.cetus.form.dto.response.ColumnsView;
import kware.apps.manager.cetus.form.dto.response.ElementType;
import kware.common.exception.SimpleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CetusFormColumnsService {

    private final CetusFormColumnsDao columnsDao;
    private final CetusColumnOptionsDao optionsDao;

    @Transactional
    public void save(ColumnsSave request) {
        // order 계산
        int nextOrder = columnsDao.findNextSortNum(request.getFormGroup());
        request.addOrder(nextOrder);

        Integer existFieldName = this.existFieldName(request.getFormGroup(), request.getName());

        if (existFieldName != null && existFieldName == 1) {
            throw new SimpleException("이미 존재하는 필드명입니다.", HttpStatus.CONFLICT);
        }

        CetusFormColumns formColumns = new CetusFormColumns(request);
        columnsDao.insert(formColumns);

        if (ElementType.from(request.getType()).requiresOption()) {
            if (request.getOptions() == null && request.getOptions().isEmpty()) {
                throw new SimpleException("옵션이 필요한 컬럼입니다.", HttpStatus.NO_CONTENT);
            }

            var options = request.getOptions();
            options.forEach(option -> {
                Integer nextSortNum = optionsDao.findNextSortNum(formColumns.getUid());
                option.addSortNum(nextSortNum);
                option.addColumnsUid(formColumns.getUid());
                option.addAuthor(formColumns.getRegUid());
                optionsDao.insert(option);
            });
        }

    }

    @Transactional
    public void change(Long uid, ColumnsChange request) {
        CetusFormColumns view = columnsDao.view(uid);
        columnsDao.update(view.changeColumns(uid, request));

        if (ElementType.from(request.getType()).requiresOption()) {
            if (request.getOptions() == null && request.getOptions().isEmpty()) {
                throw new SimpleException("옵션이 필요한 컬럼입니다.", HttpStatus.NO_CONTENT);
            }

            var options = request.getOptions();
            options.forEach(option -> {
                option.addColumnsUid(view.getUid());
                option.addAuthor(view.getRegUid());
                if (option.getUid() != null) {
                    optionsDao.update(option);
                } else {
                    Integer nextSortNum = optionsDao.findNextSortNum(uid);
                    option.addSortNum(nextSortNum);
                    optionsDao.insert(option);
                }
            });
        }
    }

    @Transactional(readOnly = true)
    public List<ColumnsPage> columns(ColumnsSearch request) {
        List<ColumnsPage> list = columnsDao.page(request);

        list.forEach(x -> {
            if (ElementType.from(x.getType()).requiresOption()) {
                List<CetusColumnOptions> options = optionsDao.list(x.getUid());
                x.addOptions(options);
            }
        });
        return list;
    }

    @Transactional(readOnly = true)
    public List<ColumnsPage> getFormGroupColumns(String formGroup) {
        List<ColumnsPage> list = columnsDao.findByTenanyAndFormGroup(new ColumnsSearch(null, formGroup));
        list.forEach(x -> {
            if (ElementType.from(x.getType()).requiresOption()) {
                List<CetusColumnOptions> options = optionsDao.list(x.getUid());
                x.addOptions(options);
            }
        });
        return list;
    }


    @Transactional(readOnly = true)
    public ColumnsView column(Long uid) {
        var data = columnsDao.findByUid(uid).orElseThrow();
        if (ElementType.from(data.getType()).requiresOption()) {
            List<CetusColumnOptions> options = optionsDao.list(data.getUid());
            data.addOptions(options);
        }
        return data;
    }


    @Transactional
    public void optionChange(Long uid, CetusColumnOptions request) {
        request.addUid(uid);
        optionsDao.update(request);
    }

    @Transactional
    public void deleteColumns(Long uid) {
        columnsDao.deleteColumns(uid);
    }

    @Transactional
    public void deleteOption(Long uid) {
        optionsDao.delete(uid);
    }

    @Transactional(readOnly = true)
    public Integer existFieldName(String formGroup, String fieldName) {

        return columnsDao.existFieldName(formGroup, fieldName, UserUtil.getUserWorkplaceUid());
    }

    @Transactional
    public void changeOrder(ColumnsOrder request) {
        for (int i = 0; i < request.getReorderedUid().size(); i++) {
            columnsDao.updateOrder(request.getReorderedUid().get(i), i + 1);
        }
    }

    @Transactional
    public void changeOptionsOrder(OptionsOrder request) {
        for (int i = 0; i < request.getNameList().size(); i++) {
            optionsDao.updateOrder(request.getColumnsUid(), request.getNameList().get(i), i + 1);
        }
    }
}
