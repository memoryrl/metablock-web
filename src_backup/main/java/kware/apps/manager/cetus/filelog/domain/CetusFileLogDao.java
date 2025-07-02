package kware.apps.manager.cetus.filelog.domain;

import cetus.bean.Page;
import cetus.bean.Pageable;
import cetus.dao.SuperDao;
import kware.apps.manager.cetus.filelog.dto.request.FileLogExcelSearch;
import kware.apps.manager.cetus.filelog.dto.response.FileLogExcelList;
import kware.apps.manager.cetus.filelog.dto.response.FileLogList;
import kware.apps.manager.cetus.user.dto.request.UserExcelSearch;
import kware.apps.manager.cetus.user.dto.response.UserExcelPage;
import org.springframework.stereotype.Component;

@Component
public class CetusFileLogDao extends SuperDao<CetusFileLog> {

    public CetusFileLogDao() {
        super("cetusFileLog");
    }

    public Page<FileLogExcelList> fileLogExcelPage(FileLogExcelSearch search, Pageable pageable) {
        return page("fileLogExcelList", "fileLogExcelCount", search, pageable);
    }
}
