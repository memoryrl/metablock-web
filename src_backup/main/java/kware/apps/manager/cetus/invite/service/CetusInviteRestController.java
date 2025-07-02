package kware.apps.manager.cetus.invite.service;

import kware.apps.manager.cetus.invite.domain.CetusInvite;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cetus/api/invite")
public class CetusInviteRestController {

    private final CetusInviteService service;

    @PostMapping
    public ResponseEntity sendInvite(@RequestBody CetusInvite request) throws MessagingException {
        service.sendSignupInvite(request);
        return ResponseEntity.ok().build();
    }
}