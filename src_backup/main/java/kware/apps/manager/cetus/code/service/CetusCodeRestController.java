package kware.apps.manager.cetus.code.service;

import cetus.bean.Page;
import cetus.bean.Pageable;
import kware.apps.manager.cetus.code.dto.request.*;
import kware.apps.manager.cetus.code.dto.response.CodePage;
import kware.apps.manager.cetus.code.dto.response.CodeView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/manager/api/code")
@RequiredArgsConstructor
public class CetusCodeRestController {

    private final CetusCodeService service;

    @GetMapping
    public ResponseEntity codePage(@Valid CodePageSearch request, Pageable pageable) {
        Page<CodePage> codePage = service.codePage(request, pageable);
        return ResponseEntity.ok(codePage);
    }

    @PostMapping
    public ResponseEntity addCode(@RequestBody @Valid CodeSave request) {
        int findCodeCnt = service.isExistCode(request.getCode());

        if (findCodeCnt > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 그룹 코드ID 입니다.");
        }

        return ResponseEntity.ok(service.addCode(request));
    }

    @PostMapping("/all-process")
    public ResponseEntity merge(@RequestBody @Valid CodeForm formRequest) {
        List<CodeChildForm> addList = formRequest.getAddList();
        Map<Object, Object> duplicatedCodes = new HashMap<>();

        if (addList != null && !addList.isEmpty()) {
            for (CodeChildForm form : addList) {
                int findCodeCnt = service.isExistCode(form.getCode());
                if (findCodeCnt > 0) {
                    duplicatedCodes.put(form.getCode(), "중복된 코드 ID 입니다.");
                }
            }
            if (!duplicatedCodes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(duplicatedCodes);
            }
        }

        service.merge(formRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/duplicate-check")
    public ResponseEntity checkCodeId(String code) {
        String message = "";
        int findCodeCnt = service.isExistCode(code);
        if (findCodeCnt > 0) message = "중복된 그룹코드ID 입니다.";
        else message = "등록 가능한 그룹코드ID 입니다.";

        Map<String, Object> map = new HashMap<>();
        map.put("cnt", findCodeCnt);
        map.put("message", message);

        return ResponseEntity.ok(map);
    }

    @GetMapping("/all-children")
    public ResponseEntity getChildCodes(String code, String useAt) {
        List<CodeView> childCodes = service.getChildCodes(code, useAt);

        if (childCodes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(childCodes);
    }

    @GetMapping("/{upperCode}")
    public ResponseEntity codesByParentCode(@PathVariable String upperCode) {
        List<CodeView> codesByParentCode = service.getCodesByParentCode(upperCode);

        if (codesByParentCode.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(codesByParentCode);
    }

    @PutMapping("/change-useAt")
    public ResponseEntity changeCodeUseAt(@RequestBody @Valid CodeChangeUseAt request) {
        service.changeCodeUseAt(request);
        return ResponseEntity.ok().build();
    }
}
