package kware.apps.manager.cetus.downloadshist.service;


import kware.apps.manager.cetus.downloadshist.dto.response.DownloadsHistList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cetus/api/download-hist")
public class CetusDownloadsHistRestController {

    private final CetusDownloadsHistService service;

    @GetMapping
    public ResponseEntity findAllUserDownloadsHistList() {
        List<DownloadsHistList> allUserDownloadsHistPage = service.findAllUserDownloadsHistList();
        return ResponseEntity.ok(allUserDownloadsHistPage);
    }

}
