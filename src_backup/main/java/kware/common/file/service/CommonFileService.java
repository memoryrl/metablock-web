package kware.common.file.service;


import cetus.bean.FileBean;
import cetus.user.UserUtil;
import cetus.util.WebUtil;
import kware.common.config.auth.PrincipalDetails;
import kware.common.config.auth.dto.SessionUserInfo;
import kware.common.file.domain.CommonFile;
import kware.common.file.domain.CommonFileDao;
import kware.common.file.domain.CommonFileLog;
import kware.common.file.domain.CommonFileState;
import kware.common.file.tus.service.FileUploadService;
import kware.common.file.tus.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonFileService {

    private final static SimpleDateFormat YYYYMM_format = new SimpleDateFormat("yyyy" + File.separator + "MM");
    private static final String IN_PROGRESS_ID = "InProgress";
    private final FileUploadService fileUploadService;
    private final CommonFileDao dao;

    @Value("${tus.server.storage.temp}")
    private String tusTemp;

    public Long generateUid() {
        return dao.key();
    }

    public CommonFile view(CommonFile bean) {
        return dao.view(bean);
    }

    public List<CommonFile> list(CommonFile bean) {
        bean.setSaved(CommonFileState.Y.name());
        List<CommonFile> resList = dao.list(bean);
        //파일경로를 암호화 해서 base64로 인코딩한 후에
        resList.stream().forEach(cfile -> cfile.setFilePath(EncryptionUtil.encrypt(cfile.getFilePath())));
        return resList;
    }

    public List<CommonFile> editList(CommonFile bean) {
        List<CommonFile> resList = dao.list(bean);
        resList.stream().forEach(cfile -> cfile.setFilePath(null));
        return resList;
    }

    public List<CommonFileLog> logList(CommonFileLog bean) {
        return dao.selectLog(bean);
    }

    public List<CommonFile> lastUpload(CommonFile bean) {
        return dao.list(bean);
    }

    @Transactional
    public ResponseEntity download(final HttpServletRequest req) {
        String fileId = req.getParameter("fileId");
        if (req.getParameter("fileId").equals("null") || req.getParameter("fileId").equals("undefined") || req.getParameter("fileId").length() == 0) {
            return ResponseEntity.ok("fileId is not found");
        }
        else {

            String download = req.getParameter("download");
            SessionUserInfo user = UserUtil.getUser(req);

            CommonFileLog commonFileLog = new CommonFileLog();
            commonFileLog.setFileId(fileId);

            CommonFile commonFile = new CommonFile();
            commonFile.setFileId(fileId);
            commonFile = dao.findFileInfo(commonFile);

            if ("Y".equals(download)) {

                commonFileLog.setFileUid(commonFile.getFileUid());
                commonFileLog.setFileId(commonFile.getFileId());

                String uri = req.getRequestURI();
                String query = req.getQueryString();
                String downloadUrl = uri + (query != null ? "?" + query : "");

                commonFileLog.setWorkerUid(user != null ? user.getUid().toString() : WebUtil.getIpAddress(req));
                commonFileLog.setWorkerNm(user != null ? user.getUserNm() : WebUtil.getIpAddress(req));
                commonFileLog.setDownloadUrl(downloadUrl);

                dao.increaseDownCnt(commonFile);  // file table - downCnt + 1
                dao.insertLog(commonFileLog);  // file log table - log insert
            }

            String realPath = commonFile.getFilePath();
            String orgFileNm = commonFile.getOrgFileNm();
            File file2 = new File(realPath);

            Resource resource = new FileSystemResource(realPath);

            if (file2.exists() && file2.isFile()) {
                /*if (log.isDebugEnabled())
                    log.debug("파일이 존재합니다. filepath:{}, orgFileNam:{}", realPath, orgFileNm);*/
            } else {
                log.error("파일이 존재하지 않습니다. filepath:{}, orgFileNam:{}", realPath, orgFileNm);
                return ResponseEntity.ok("file is not existed");
            }

            String fileNameOrg = null;
            try {
                fileNameOrg = URLEncoder.encode(orgFileNm, "UTF-8").replaceAll("\\+", "%20");
            } catch (UnsupportedEncodingException e) {
            }
            String disposition = "inline";
            if("Y".equals(download)) disposition = "attachment;filename=" + fileNameOrg + ";";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", disposition)
                    .body(resource);
        }
    }

    public Boolean checkFile(HttpServletRequest req) {
        String fileId = req.getParameter("fileId");
        if (req.getParameter("fileId").equals("null") || req.getParameter("fileId").equals("undefined") || req.getParameter("fileId").length() == 0) return Boolean.FALSE;
        else {

            CommonFile commonFile = new CommonFile();
            commonFile.setFileId(fileId);
            commonFile = dao.findFileInfo(commonFile);

            String realPath = commonFile.getFilePath();
            File file2 = new File(realPath);

            if (file2.exists() && file2.isFile()) return Boolean.TRUE;

            return Boolean.FALSE;
        }
    }

    @Transactional
    public ResponseEntity download2(final HttpServletRequest req) {
        String fileId = req.getParameter("fileId");
        if (req.getParameter("fileId").equals("null") || req.getParameter("fileId").equals("undefined") || req.getParameter("fileId").length() == 0) {
            return ResponseEntity.ok("fileId is not found");
        }
        else {

            File file2 = new File(tusTemp + File.separator + fileId);
            String filePath = file2.getAbsolutePath();
            String fileName = file2.getName();

            Resource resource = new FileSystemResource(filePath);

            if (file2.exists() && file2.isFile()) {
                /*if (log.isDebugEnabled())
                    log.debug("파일이 존재합니다. filepath:{}, orgFileNam:{}", filePath, fileName);*/
            } else {
                log.error("파일이 존재하지 않습니다. filepath:{}, orgFileNam:{}", filePath, fileName);
            }

            String disposition = "inline";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", disposition)
                    .body(resource);
        }
    }

    @Transactional
    public <T extends FileBean> Long processFileBean(T bean, SessionUserInfo user, Long fileUid) {
        if (bean.getFileAdd() != null) {
            for (CommonFile f : bean.getFileAdd()) {
                if(fileUid == null) {
                    fileUid = this.generateUid();
                }
                f.setRegId(user.getUserId());
                f.setFileUid(fileUid);
                try {
                    f = this.moveToDetaultStorage(f);
                } catch (IOException e) {
                    log.error(e.toString(), e);
                    continue;
                }
                f.setSaved(CommonFileState.Y.name());
                dao.insert(f);
            }
        }

        if (bean.getFileDel() != null) {
            for (CommonFile f : bean.getFileDel()) {
                f.setFileId(f.getFileId());
                dao.delete(f); //논리적인 삭제: 물리적인 파일을 삭제하지 않는다.
            }
        }

        return fileUid;
    }

    @Transactional
    public Long processFileSeparately(List<CommonFile> fileAdd, List<CommonFile> fileDel, Long fileUid) {
        if (fileAdd != null) {
            for (CommonFile f : fileAdd) {
                if(fileUid == null) {
                    fileUid = this.generateUid();
                }

                String userId = "비화원유저";
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (UserUtil.isAuthenticated(authentication)) {
                    PrincipalDetails details = (PrincipalDetails) authentication.getPrincipal();
                    userId = details.getUser().getUserId();
                }
                f.setRegId(userId);
                f.setFileUid(fileUid);

                try {
                    f = this.moveToDetaultStorage(f);
                } catch (IOException e) {
                    log.error(e.toString(), e);
                    continue;
                }
                f.setSaved(CommonFileState.Y.name());
                dao.insert(f);
            }
        }

        if (fileDel != null) {
            for (CommonFile f : fileDel) {
                f.setFileId(f.getFileId());
                dao.delete(f); //논리적인 삭제: 물리적인 파일을 삭제하지 않는다.
            }
        }

        return fileUid;
    }


    /**
     * 현재 파일이 temp경로에 있으면 파일을 절대경로로 변경하고 파일 경로 및 사이즈 설정
     *
     * @param cFile
     * @return
     */
    private CommonFile makeFileInfo(CommonFile cFile) throws IOException {
        if (CommonFileState.N.name().equals(cFile.getSaved())) { //저장전 파일
            String dir = fileUploadService.getStoragePath_temp();
            String fileId = cFile.getFileId();
            File tempFile = new File(dir + File.separator + fileId);
            if (!tempFile.exists() && !tempFile.isFile()) {
                log.error("파일경로에 파일이 없습니다. fileId: {}", cFile.getFileId());
                throw new FileNotFoundException("File not found:" + tempFile.getPath());
            }
            cFile.setFileSize(tempFile.length());
            cFile.setFilePath(tempFile.getAbsolutePath());
            //cFile.setFileUrl(makeUr(tempFile.getAbsolutePath()));
        } else { // 저장했던 파일의 사이즈 만 다시 확인하는 정도
            if (cFile.getFileSize() == null || cFile.getFileSize() == 0) {
                File tempFile = new File(cFile.getFilePath());
                if (!tempFile.exists() && !tempFile.isFile()) {
                    log.error("파일경로에 파일이 없습니다. fileId: {}", cFile.getFileId());
                    throw new FileNotFoundException("File not found:" + tempFile.getPath());
                }
                cFile.setFileSize(tempFile.length());
                //cFile.setFileUrl(makeUr(tempFile.getAbsolutePath()));
            }

        }
        return cFile;
    }


    /**
     * CommonFile의 filepath의 정보를 기본 날짜별 저장소로 이동: 파일 이동, 사이즈, 경로 설정
     * 외부에서 개별적을 사용할 수 있도록 함, 단일파일 처리
     */
    public CommonFile moveToDetaultStorage(CommonFile cFile) throws IOException {
        CommonFile mFile = makeFileInfo(cFile);

        //Path tempFilePath = Paths.get(cFile.getFilePath());
        String dataFolder = fileUploadService.getStoragePath_data();

        Date cd = new Date();
        String subfolder = YYYYMM_format.format(cd);
        String reFilename = mFile.getFileId();// + "_"+ filename;
        File targetFile = new File(dataFolder + File.separator + subfolder, reFilename);

        mFile = moveToRequestStorage(mFile, targetFile.getAbsolutePath());

        return mFile;
    }

    /**
     * CommonFile의 filepath의 정보를 요청한 파일 폴더로 이동: 파일 이동, 사이즈, 경로 설정
     * * 외부에서 개별적을 사용할 수 있도록 함, 단일파일 처리
     *
     * @param cFile
     * @param absolutePath
     * @return
     * @throws IOException
     */
    public CommonFile moveToRequestStorage(CommonFile cFile, String absolutePath) throws IOException {
        cFile = makeFileInfo(cFile);

        Path tempFilePath = Paths.get(cFile.getFilePath());

        File targetFile = new File(absolutePath);

        if (!targetFile.getParentFile().exists())
            targetFile.getParentFile().mkdirs();

        if (targetFile.exists() && targetFile.isFile())
            targetFile.delete();

        Files.move(tempFilePath, targetFile.toPath());

        if (log.isDebugEnabled())
            log.debug("moveTempToStoragee:{}", absolutePath);

        cFile.setFilePath(absolutePath);
        //cFile.setFileUrl(makeUr(absolutePath));
        return cFile;
    }

    /**
     * 논리적 파일 삭제: CommonFile의 FileId 필수
     */
    public int deleteFile(CommonFile cFile) {
        if (cFile.getFileId() != null)
            return dao.delete(cFile);
        else {
            log.error("Error 원인: FileId is null");
            return -1;
        }
    }


    /**
     * default storage로 데이터를 저장
     */
    public int insert(CommonFile bean) {
        try {
            bean = moveToDetaultStorage(bean);
            bean.setSaved(CommonFileState.Y.name());
        } catch (IOException e) {
            log.error("insert moveToDefaultStroage Error", e);
        }
        return dao.insert(bean);
    }

    /**
     * request storage로 데이터를 저장
     */
    public int insert(CommonFile bean, String absolutePath) {
        try {
            bean = moveToRequestStorage(bean, absolutePath);
            bean.setSaved(CommonFileState.Y.name());
        } catch (IOException e) {
            log.error("insert moveToRequestStorage Error", e);
        }

        return dao.insert(bean);
    }
}
