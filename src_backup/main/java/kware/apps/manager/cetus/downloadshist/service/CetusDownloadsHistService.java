package kware.apps.manager.cetus.downloadshist.service;


import cetus.user.UserUtil;
import kware.apps.manager.cetus.downloadshist.domain.CetusDownloadsHist;
import kware.apps.manager.cetus.downloadshist.domain.CetusDownloadsHistDao;
import kware.apps.manager.cetus.downloadshist.dto.response.DownloadsHistList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CetusDownloadsHistService {

    private final CetusDownloadsHistDao dao;

    @Transactional
    public void saveDownloadsHistUser(String targetCd, Long fileUid) {
        dao.insertDownloadsHist(new CetusDownloadsHist(targetCd, null, fileUid, UserUtil.getUser().getUid()));
    }

    @Transactional(readOnly = true)
    public List<DownloadsHistList> findAllUserDownloadsHistList() {
        return dao.getAllUserDownloadsHistList(UserUtil.getUser().getUid());
    }

    @Transactional
    public void deleteDownloadHistUser(Long fileUid) {
        dao.deleteDownloadHistUser(fileUid);
    }
}
