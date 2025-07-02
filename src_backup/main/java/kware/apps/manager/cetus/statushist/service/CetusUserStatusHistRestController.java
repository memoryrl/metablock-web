package kware.apps.manager.cetus.statushist.service;


import cetus.bean.Page;
import cetus.bean.Pageable;
import kware.apps.manager.cetus.statushist.dto.request.UserStatusHistExcelSearch;
import kware.apps.manager.cetus.statushist.dto.request.UserStatusHistSearch;
import kware.apps.manager.cetus.statushist.dto.response.UserStatusHistList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cetus/api/user-status-hist")
public class CetusUserStatusHistRestController {

    private final CetusUserStatusHistService service;

    @GetMapping
    public ResponseEntity findAllUserStatusHistPage(@Valid UserStatusHistSearch search, Pageable pageable) {
        Page<UserStatusHistList> allUserStatusHistPage = service.findAllUserStatusHistPage(search, pageable);
        return ResponseEntity.ok(allUserStatusHistPage);
    }

    @GetMapping("/excel")
    public ResponseEntity renderUserExcelPage(@Valid UserStatusHistExcelSearch search) {
        service.renderEXCEL(search);
        return ResponseEntity.ok().build();
    }
}
