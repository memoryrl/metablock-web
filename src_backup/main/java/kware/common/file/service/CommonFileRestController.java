package kware.common.file.service;

import cetus.Response;
import cetus.user.UserUtil;
import kware.common.config.auth.dto.SessionUserInfo;
import kware.common.file.domain.CommonFile;
import kware.common.file.domain.CommonFileLog;
import kware.common.file.domain.CommonFileState;
import kware.common.file.tus.util.SimpleJwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cetus/files")
public class CommonFileRestController {
    
    private final CommonFileService fileService;
    
    /**
     * 임시 저장 파일 목록
     * @return
     */
    @GetMapping("temp/list.json")
    public Response lastUpload(CommonFile bean) {
        bean.setSaved(CommonFileState.N.name());
        bean.setFileUid(0L);
        return Response.ok(fileService.lastUpload(bean));
    }
    
    /**
     * 수정 파일 목록 (임시 파일 포함)
     * @param bean
     * @return
     */
    @GetMapping("edit/list.json")
    public Response editList(CommonFile bean) {
        return Response.ok(fileService.editList(bean));
    }
    
    /**
     * 저장된 파일 목록
     * @param bean
     * @return
     */
    @GetMapping("list.json")
    public Response list(CommonFile bean) {
        return Response.ok(fileService.list(bean));
    }
    
    
    /**
     * 저장된 파일 상세정보
     * @param bean
     * @return
     */
    @GetMapping("view.json")
    public Response view(CommonFile bean) {
        return Response.ok(fileService.view(bean));
    }

    /**
     * 파일 다운로드 기록
     * @param bean
     * @return
     */
    @GetMapping("log/list.json")
    public Response logList(CommonFileLog bean) {
        return Response.ok(fileService.logList(bean));
    }

    /**
     * jwt token으로 대용량파일 관련 보안처리용
     */
    @GetMapping("bigfile/jwttoken.json")
    public Response jwttoken(HttpServletRequest req) {

        SessionUserInfo user = UserUtil.getUser(req);

        Long userUid = user.getUid();
        Map<String,Object> claimsMap = new HashMap<String,Object>();
        claimsMap.put("userUid", userUid.toString());
        //endpointUid는 임시로 사용함, 기존로직을 수정하면서 임시로 처리함: 지우면 오류남.
        claimsMap.put("endpointUid", (new Long(System.currentTimeMillis())).toString());

        //subject 이외의 정보를 넣기 위함
        String webToken = SimpleJwtUtil.getJwtToken(claimsMap);
        claimsMap.clear();
        claimsMap = null;
        //하나의 subject로 처리할 경우
        //String webToken = SimpleJwtUtil.getJwtToken(userUid);


        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("jwtToken", webToken);


        return Response.ok(retMap);
    }
}
