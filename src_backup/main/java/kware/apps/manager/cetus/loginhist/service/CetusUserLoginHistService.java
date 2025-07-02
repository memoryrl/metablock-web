package kware.apps.manager.cetus.loginhist.service;


import cetus.bean.Page;
import cetus.bean.Pageable;
import kware.apps.manager.cetus.enumstatus.DownloadTargetCd;
import kware.apps.manager.cetus.loginhist.domain.CetusUserLoginHist;
import kware.apps.manager.cetus.loginhist.domain.CetusUserLoginHistDao;
import kware.apps.manager.cetus.loginhist.dto.request.UserLoginHistExcelSearch;
import kware.apps.manager.cetus.loginhist.dto.request.UserLoginHistSave;
import kware.apps.manager.cetus.loginhist.dto.request.UserLoginHistSearch;
import kware.apps.manager.cetus.loginhist.dto.response.UserLoginHistExcelList;
import kware.apps.manager.cetus.loginhist.dto.response.UserLoginHistList;
import kware.common.excel.ExcelCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CetusUserLoginHistService {

    private final CetusUserLoginHistDao dao;
    private final ExcelCreate excelCreate;

    @Transactional
    public void saveUserLoginHist(UserLoginHistSave request) {
        dao.insert(new CetusUserLoginHist(request));
    }

    @Transactional(readOnly = true)
    public Page<UserLoginHistList> findAllUserLoginHistPage(UserLoginHistSearch search, Pageable pageable) {
        return dao.page("userLoginHistList", "userLoginHistCount", search, pageable);
    }

    @Async
    public void renderEXCEL(UserLoginHistExcelSearch search) {
        excelCreate.createExcelFile(
                UserLoginHistExcelList.class,
                p -> dao.userLoginHistExcelPage(search, p),
                DownloadTargetCd.LOGIN_HIST
        );
    }
}
