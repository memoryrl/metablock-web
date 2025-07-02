package kware.apps.manager.cetus.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cetus/api/custom-file")
public class CetusFileRestController {

    private final CetusFileService service;

    @GetMapping("/downloading-list")
    public ResponseEntity findUserDownloadingList() {
        int downloading = service.countUserDownloading();
        return ResponseEntity.ok(downloading);
    }

    @PostMapping({"/{fileId}"})
    public ResponseEntity<ByteArrayResource> download(@PathVariable("fileId") String fileId, final HttpServletRequest req) throws IOException {
        return service.download(fileId, req);
    }

    @DeleteMapping("/{fileUid}")
    public ResponseEntity deleteFile(@PathVariable("fileUid") Long fileUid) {
        service.deleteFile(fileUid);
        return ResponseEntity.ok().build();
    }
}
