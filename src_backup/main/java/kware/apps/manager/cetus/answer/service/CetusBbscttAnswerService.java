package kware.apps.manager.cetus.answer.service;


import cetus.bean.Page;
import cetus.bean.Pageable;
import cetus.util.HtmlUtil;
import kware.apps.manager.cetus.answer.domain.CetusBbscttAnswer;
import kware.apps.manager.cetus.answer.domain.CetusBbscttAnswerDao;
import kware.apps.manager.cetus.answer.dto.request.*;
import kware.apps.manager.cetus.answer.dto.response.AnswerExcelList;
import kware.apps.manager.cetus.answer.dto.response.AnswerList;
import kware.apps.manager.cetus.enumstatus.DownloadTargetCd;
import kware.common.excel.ExcelCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CetusBbscttAnswerService {

    private final CetusBbscttAnswerDao dao;
    private final ExcelCreate excelCreate;

    @Value("${cetus.base-url}")
    private String baseUrl;

    @Transactional
    public void saveAnswer(AnswerSave request) {
        dao.insert(new CetusBbscttAnswer(request));
    }

    @Transactional(readOnly = true)
    public List<AnswerList> findAllAnswerList(Long bbscttUid) {
        return dao.getAllAnswerList(bbscttUid);
    }

    @Transactional
    public void deleteAnswer(Long uid) {
        dao.delete(uid);
    }

    @Transactional
    public Page<AnswerList> findAllAnswerPage(AnswerSearch search, Pageable pageable) {
        return dao.page("answerBbsRegPageList", "answerBbsRegPageListCount", search, pageable);
    }

    @Transactional
    public void deleteAnswers(AnswerDelete request) {
        for(Long uid : request.getUids()) {
            dao.delete(uid);;
        }
    }

    @Async
    public void renderEXCEL(AnswerExcelSearch search) {
        search.setBaseUrl(baseUrl);
        excelCreate.createExcelFileHtmlCustom(
                AnswerExcelList.class,
                p -> dao.answerExcelPage(search, p),
                DownloadTargetCd.USER_ANSWER
        );
    }

    @Transactional
    public void changeAnswer(Long uid, AnswerChange request) {
        dao.update(new CetusBbscttAnswer(uid, request));
    }
}
