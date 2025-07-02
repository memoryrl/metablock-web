package kware.apps.manager.cetus.program.service;

import cetus.bean.Page;
import cetus.bean.Pageable;
import kware.apps.manager.cetus.program.dto.request.MenuProgrmInfoSearch;
import kware.apps.manager.cetus.program.dto.request.ProgramChange;
import kware.apps.manager.cetus.program.dto.request.ProgramSave;
import kware.apps.manager.cetus.program.dto.request.ProgrmInfoSearch;
import kware.apps.manager.cetus.program.dto.response.MenuProgrmInfoList;
import kware.apps.manager.cetus.program.dto.response.ProgrmInfoList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cetus/api/program")
public class CetusProgrmInfoRestController {

    private final CetusProgrmInfoService service;

    @GetMapping("/menu")
    public ResponseEntity findAllMenuProgramPage(@Valid MenuProgrmInfoSearch search, Pageable pageable) {
        Page<MenuProgrmInfoList> allProgramPage = service.findAllMenuProgramPage(search, pageable);
        return ResponseEntity.ok(allProgramPage);
    }

    @GetMapping
    public ResponseEntity findAllProgramPage(@Valid ProgrmInfoSearch search, Pageable pageable) {
        Page<ProgrmInfoList> allProgramPage = service.findAllProgramPage(search, pageable);
        return ResponseEntity.ok(allProgramPage);
    }

    @PostMapping
    public ResponseEntity saveProgram(@RequestBody @Valid ProgramSave request) {
        service.saveProgram(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{uid}")
    public ResponseEntity changeProgram(@PathVariable("uid") Long uid, @RequestBody @Valid ProgramChange request) {
        service.changeProgram(uid, request);
        return ResponseEntity.ok().build();
    }
}
