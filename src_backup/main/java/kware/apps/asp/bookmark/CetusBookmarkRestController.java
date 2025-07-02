package kware.apps.asp.bookmark;

import cetus.Response;
import cetus.user.UserUtil;
import kware.apps.asp.bookmark.dto.request.CetusBookmarkToggle;
import kware.apps.asp.bookmark.dto.request.CetusSearchBookmark;
import kware.common.config.auth.dto.SessionUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/contents/api/bookmark")
@RequiredArgsConstructor
public class CetusBookmarkRestController {

    private final CetusBookmarkService service;

    @GetMapping("/list")
    public Response page() {
        SessionUserInfo user = UserUtil.getUser();
        if (user == null) {
            return Response.ok(null, "user is not defined");
        }
        CetusSearchBookmark request = new CetusSearchBookmark();
        request.setUserUid(user.getUid());
        List<CetusBookmark> list = service.getList(request);
        return Response.ok(list);
    }

    @PostMapping("/toggle")
    public Response toggleLike(@RequestBody @Valid CetusBookmarkToggle request) {

        SessionUserInfo user = UserUtil.getUser();

        if (user == null) {
            return Response.ok("user is not defined");
        }

        return Response.ok(service.toggleLike(request, user.getUid()));
    }

    @DeleteMapping("/delete")
    public Response delete(@Valid CetusBookmark request) {
        SessionUserInfo user = UserUtil.getUser();
        if (user == null) {
            return Response.ok("user is not defined");
        }
        request.setUserUid(user.getUid());
        service.delete(request);
        return Response.ok();
    }
}
