package kware.apps.manager.cetus.loginhist.service;


import cetus.bean.Page;
import cetus.bean.Pageable;
import kware.apps.manager.cetus.loginhist.dto.request.UserLoginHistExcelSearch;
import kware.apps.manager.cetus.loginhist.dto.request.UserLoginHistSearch;
import kware.apps.manager.cetus.loginhist.dto.response.UserLoginHistList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cetus/api/login-hist")
public class CetusUserLoginHistRestController {

    private final CetusUserLoginHistService service;

    @GetMapping
    public ResponseEntity findAllUserLoginHistPage(@Valid UserLoginHistSearch search, Pageable pageable) {
        Page<UserLoginHistList> allUserLoginHistPage = service.findAllUserLoginHistPage(search, pageable);
        return ResponseEntity.ok(allUserLoginHistPage);
    }

    @GetMapping("/excel")
    public ResponseEntity renderUserExcelPage(@Valid UserLoginHistExcelSearch search) {
        service.renderEXCEL(search);
        return ResponseEntity.ok().build();
    }
}
