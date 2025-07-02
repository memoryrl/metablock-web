package kware.apps.manager.cetus.bbsctt.service;


import cetus.bean.Page;
import cetus.bean.Pageable;
import cetus.user.UserUtil;
import cetus.util.CookieUtil;
import kware.apps.manager.cetus.bbsctt.domain.CetusBbsctt;
import kware.apps.manager.cetus.bbsctt.domain.CetusBbscttDao;
import kware.apps.manager.cetus.bbsctt.dto.request.*;
import kware.apps.manager.cetus.bbsctt.dto.response.BbscttExcelList;
import kware.apps.manager.cetus.bbsctt.dto.response.BbscttList;
import kware.apps.manager.cetus.bbsctt.dto.response.BbscttRecentList;
import kware.apps.manager.cetus.bbsctt.dto.response.BbscttView;
import kware.apps.manager.cetus.enumstatus.DownloadTargetCd;
import kware.common.excel.ExcelCreate;
import kware.common.file.service.CommonFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CetusBbscttService {

    private final CetusBbscttDao dao;
    private final CommonFileService commonFileService;
    private final ExcelCreate excelCreate;

    @Value("${cetus.base-url}")
    private String baseUrl;

    @Transactional(readOnly = true)
    public List<BbscttRecentList> findRecentBbsctt(int recent) {
        Long workplaceUid = UserUtil.getUserWorkplaceUid();
        return dao.getRecentBbsctt(new BbscttRecentSearch(workplaceUid, recent));
    }

    @Transactional(readOnly = true)
    public Page<BbscttList> findAllBbscttPage(BbscttSearch search, Pageable pageable) {
        return dao.page("bbscttPageList", "bbscttPageListCount", search, pageable);
    }

    @Transactional
    public void saveBbsctt(BbscttSave request) {
        Long fileUid = commonFileService.processFileBean(request, UserUtil.getUser(), null);
        dao.insert(new CetusBbsctt(request, fileUid));
    }

    @Transactional(readOnly = true)
    public CetusBbsctt view(Long bbscttUid) {
        return dao.view(bbscttUid);
    }

    @Transactional
    public void changeBbsctt(Long bbscttUid, BbscttChange request) {
        Long fileUid = request.getFileUid();
        fileUid = commonFileService.processFileBean(request, UserUtil.getUser(), fileUid);
        CetusBbsctt view = dao.view(bbscttUid);
        dao.update(view.changeBbsctt(bbscttUid, request, fileUid));
    }

    @Transactional
    public void deleteBbsctt(Long uid) {
        dao.delete(uid);
    }

    @Transactional
    public void increaseViewCount(Long bbscttUid, HttpServletRequest req, HttpServletResponse res) {

        String userId = UserUtil.getUser().getUserId();
        String cookie = CookieUtil.getCookie(req, "bbsctt" + userId);
        if (cookie == null) {
            CookieUtil.createCookie(res, "bbsctt" + userId, "[" + userId + "]", 60);
            dao.increaseViewCount(bbscttUid);
        }
    }

    @Transactional(readOnly = true)
    public BbscttView findViewByBbscttUid(Long bbscttUid) {
        return dao.getViewByBbscttUid(bbscttUid);
    }

    @Transactional
    public void changeBbscttOpenAt(BbscttChangeOpenAt request) {
        for(Long uid : request.getUids()) {
            dao.updateBbscttOpenAt(new CetusBbsctt(uid, request.getOpenAt()));
        }
    }

    @Transactional
    public void deleteBbsctts(BbscttDelete request) {
        for(Long uid : request.getUids()) {
            dao.delete(uid);
        }
    }

    @Async
    public void renderEXCEL(BbscttExcelSearch search) {
        search.setBaseUrl(baseUrl);
        excelCreate.createExcelFile(
                BbscttExcelList.class,
                p -> dao.bbscttExcelPage(search, p),
                DownloadTargetCd.USER_BBSCTT
        );
    }
}
