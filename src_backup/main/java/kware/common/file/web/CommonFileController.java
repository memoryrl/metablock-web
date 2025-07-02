package kware.common.file.web;

import kware.common.file.domain.CommonStaticFiles;
import kware.common.file.service.CommonFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/cetus/files")
public class CommonFileController {

    private final CommonStaticFiles staticFiles;
    private final CommonFileService fileService;

    // download!
    @GetMapping(value = "/static/{id}")
    public ResponseEntity<?> download(@PathVariable String id) throws UnsupportedEncodingException {
        String orgFileNm = staticFiles.get(id);
        ClassPathResource staticFile = new ClassPathResource("/static/files/" + id);

        String fileNameOrg = URLEncoder.encode(orgFileNm, "UTF-8").replaceAll("\\+", "%20");
        String disposition = "attachment;filename=" + fileNameOrg + ";";
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", disposition)
                .body(staticFile);
    }
    
    /* 운영 안함 */
/*
    @RequestMapping(value = {"upload", "upload/**"},
            method = {RequestMethod.POST, RequestMethod.OPTIONS})
    public void process(final HttpServletRequest req, final HttpServletResponse res) throws IOException, TusException {
        // 각 파라미터에 대한 key-value 쌍을 출
        log.info("process");
        uploadService.process(req, res);
    }

    @DeleteMapping({"upload", "upload/**"})
    public void delete(final HttpServletRequest req, final HttpServletResponse res) throws IOException, TusException {
        uploadService.process(req, res);
        fileService.deleteFile(req);
    }

    @RequestMapping(value = {"upload", "upload/**"},
            method = {RequestMethod.HEAD, RequestMethod.PATCH})
    public void upload(final HttpServletRequest req, final HttpServletResponse res) throws IOException, TusException {
        log.info("upload");
        uploadService.process(req, res);
        //fileService.saveFile(req);
    }
*/
 /*   
    @GetMapping({"upload", "upload/**"})
    public void download(final HttpServletRequest req, final HttpServletResponse res) throws IOException, TusException {
        String fileUid = req.getParameter("fileUid");
        log.info("> fileUid : {}", fileUid);
        uploadService.process(req, res);
        if(fileUid != null) fileService.download(req);
    }
    */

    @GetMapping({"download"})
    public ResponseEntity<Resource> download(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        return fileService.download(req);
    }

    @GetMapping({"download2"})
    public ResponseEntity<Resource> download2(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        return fileService.download2(req);
    }

    @GetMapping("/check-file")
    public ResponseEntity<Boolean> checkFile(final HttpServletRequest req, final HttpServletResponse res) {
        return ResponseEntity.ok(fileService.checkFile(req));
    }
}