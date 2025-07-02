package kware.apps.manager.cetus.file.service;

import cetus.user.UserUtil;
import cetus.util.WebUtil;
import kware.apps.manager.cetus.file.domain.CetusFile;
import kware.apps.manager.cetus.file.domain.CetusFileDao;
import kware.apps.manager.cetus.filelog.domain.CetusFileLog;
import kware.apps.manager.cetus.filelog.service.CetusFileLogService;
import kware.common.config.auth.dto.SessionUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CetusFileService {

    private final CetusFileDao dao;
    private final CetusFileLogService logService;

    @Value("${cetus.download-path-prefix}")
    private String downloadPrefix;

    @Transactional(readOnly = true)
    public int countUserDownloading() {
        return dao.countUserDownloading(UserUtil.getUser().getUid());
    }

    @Transactional
    public void saveFile(Long fileUid, String orgFileNm) {
        String userId = UserUtil.getUser().getUserId();
        Long userUid = UserUtil.getUser().getUid();
        if(!this.checkIfUserDownloadingFile(userUid)) {
            dao.insertFile(new CetusFile(fileUid, "0", orgFileNm, userId));
        }
    }

    @Transactional(readOnly = true)
    public boolean checkIfUserDownloadingFile(Long userUid) {
        int count = dao.countUserDownloading(userUid);
        return count > 0;
    }

    @Transactional
    public void deleteNotDownloadFile(Long fileUid) {
        dao.deleteNotDownloadFile(fileUid);
    }

    @Transactional
    public void updateDownloadFile(Long fileUid, File tempFile) throws IOException {
        String fileId =  UUID.randomUUID().toString();
        String fileNm = tempFile.getName();
        String filePath = tempFile.toPath().toString();
        String fileUrl = downloadPrefix + fileId;
        Long fileSize = Files.size(tempFile.toPath());
        String extension = tempFile.getName().substring(tempFile.getName().lastIndexOf(".")+1, tempFile.getName().length());

        CetusFile cf = CetusFile.updateDownloadFile()
                                .fileUid(fileUid)
                                .fileId(fileId)
                                .fileNm(fileNm)
                                .filePath(filePath)
                                .fileUrl(fileUrl)
                                .fileSize(fileSize)
                                .extension(extension)
                                .build();
        dao.updateDownloadFile(cf);
    }

    @Transactional(readOnly = true)
    public CetusFile findFileInfoById(String fileId) {
        return dao.getFileInfoById(fileId);
    }

    @Transactional
    public void increaseDownCnt(String fileId) {
        dao.updateDownCnt(fileId);
    }

    @Transactional
    public ResponseEntity<ByteArrayResource> download(String fileId, final HttpServletRequest req) {

        SessionUserInfo user = UserUtil.getUser(req);
        CetusFile fileBean = this.findFileInfoById(fileId);
        String workerUid = user != null ? user.getUid().toString() : WebUtil.getIpAddress(req);
        String workerNm = user != null ? user.getUserNm() : WebUtil.getIpAddress(req);

        try {
            File tempFile = Paths.get(fileBean.getFilePath()).toFile();
            if (!tempFile.exists()) {
                throw new FileNotFoundException("File does not exist: " + fileBean.getFilePath());
            }

            String downloadUrl = downloadPrefix + fileId;
            CetusFileLog logBean = new CetusFileLog(fileBean.getFileUid(), fileId, workerUid, workerNm, downloadUrl);
            this.increaseDownCnt(fileId);
            logService.saveFileLog(logBean);

            ByteArrayResource resource = null;
            try {
                resource = new ByteArrayResource(Files.readAllBytes(tempFile.toPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition
                    .builder("attachment")
                    .filename(fileBean.getOrgFileNm(), StandardCharsets.UTF_8)
                    .build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(tempFile.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (FileNotFoundException e) {
            log.error("file not found : {}", e.getMessage());
            e.printStackTrace();
            return ResponseEntity.notFound().build();           // 파일을 찾지 못한 경우 404 응답 반환
        }
    }

    @Transactional
    public void deleteFile(Long fileUid) {
        dao.deleteFile(fileUid);
    }

}
