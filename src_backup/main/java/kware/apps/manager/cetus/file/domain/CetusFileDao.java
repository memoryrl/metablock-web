package kware.apps.manager.cetus.file.domain;

import cetus.dao.SuperDao;
import org.springframework.stereotype.Component;

@Component
public class CetusFileDao extends SuperDao<CetusFile> {

    public CetusFileDao() {
        super("cetusFile");
    }

    public int insertFile(CetusFile bean) {
        return insert("insertFile", bean);
    }

    public int countUserDownloading(Long userUid) {
        return selectOne("countUserDownloading", userUid);
    }

    public int deleteNotDownloadFile(Long fileUid) {
        return delete("deleteNotDownloadFile", fileUid);
    }

    public int updateDownloadFile(CetusFile bean) {
        return update("updateDownloadFile", bean);
    }

    public CetusFile getFileInfoById(String fileId) {
        return selectOne("getFileInfoById", fileId);
    }

    public int updateDownCnt(String fileId) {
        return update("updateDownCnt", fileId);
    }

    public int deleteFile(Long fileUid) {
        return delete("deleteFile", fileUid);
    }
}
