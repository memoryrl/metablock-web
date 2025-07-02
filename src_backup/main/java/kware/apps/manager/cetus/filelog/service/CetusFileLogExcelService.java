package kware.apps.manager.cetus.filelog.service;

import kware.apps.manager.cetus.enumstatus.DownloadTargetCd;
import kware.apps.manager.cetus.filelog.domain.CetusFileLogDao;
import kware.apps.manager.cetus.filelog.dto.request.FileLogExcelSearch;
import kware.apps.manager.cetus.filelog.dto.response.FileLogExcelList;
import kware.common.excel.ExcelCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CetusFileLogExcelService {

    private final ExcelCreate excelCreate;
    private final CetusFileLogDao dao;

    @Async
    public void renderEXCEL(FileLogExcelSearch search) {
        excelCreate.createExcelFile(
                FileLogExcelList.class,
                p -> dao.fileLogExcelPage(search, p),
                DownloadTargetCd.DOWNLOAD_HIST
        );
    }
}
