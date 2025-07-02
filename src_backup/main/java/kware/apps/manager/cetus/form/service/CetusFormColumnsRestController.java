package kware.apps.manager.cetus.form.service;

import cetus.Response;
import kware.apps.manager.cetus.form.dto.request.*;
import kware.apps.manager.cetus.form.dto.response.ColumnsPage;
import kware.apps.manager.cetus.form.dto.response.ColumnsView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value="/cetus/api/form")
@RequiredArgsConstructor
public class CetusFormColumnsRestController {
    private final CetusFormColumnsService service;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid ColumnsSave request) {
        service.save(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{uid}")
    public ResponseEntity<Void> update(@PathVariable Long uid, @RequestBody @Valid ColumnsChange request) {
        service.change(uid, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ColumnsPage>> page(ColumnsSearch request) {
        return ResponseEntity.ok(service.columns(request));
    }

    @GetMapping("/{uid}")
    public ResponseEntity<ColumnsView> columns(@PathVariable Long uid) {
        return ResponseEntity.ok(service.column(uid));
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> columnsDelete(@PathVariable Long uid) {
        service.deleteColumns(uid);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/option/{uid}")
    public ResponseEntity<Void> optionDelete(@PathVariable Long uid) {
        service.deleteOption(uid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-name")
    public Response checkName(@RequestParam("name") String name, @RequestParam("formGroup") String formGroup) {
        String message = "";
        Integer findUserCnt = service.existFieldName(formGroup, name);
        if(findUserCnt == 1) message = "이미 등록된 필드명입니다. 다른 필드명을 입력해주세요";
        else if(findUserCnt == 0) message = "사용 가능한 필드명 입니다.";

        return Response.ok(findUserCnt, message);
    }

    @PutMapping("/order")
    public ResponseEntity<Void> changeOrder(@RequestBody @Valid ColumnsOrder request) {
        service.changeOrder(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/options/order")
    public ResponseEntity<Void> changeOptionsOrder(@RequestBody @Valid OptionsOrder request) {
        service.changeOptionsOrder(request);
        return ResponseEntity.ok().build();
    }
}
