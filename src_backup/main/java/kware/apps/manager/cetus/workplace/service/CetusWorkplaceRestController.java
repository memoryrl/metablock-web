package kware.apps.manager.cetus.workplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/cetus/api/workplace")
@RequiredArgsConstructor
public class CetusWorkplaceRestController {

    private final CetusWorkplaceService service;
}
