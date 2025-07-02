package kware.apps.manager.cetus.menu.service;

import kware.apps.manager.cetus.menu.dto.request.MenuChange;
import kware.apps.manager.cetus.menu.dto.request.MenuSave;
import kware.apps.manager.cetus.menu.dto.request.MenuTreeSearch;
import kware.apps.manager.cetus.menu.dto.response.MenuTreeList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cetus/api/menu")
public class CetusMenuInfoRestController {

    private final CetusMenuInfoService service;

    /*
    * [메뉴 관리]에 이용되는 API
    * */
    @GetMapping("/tree")
    public ResponseEntity tree(MenuTreeSearch search) {
        List<MenuTreeList> list = service.getMenuTreeList(search);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity saveMenuTreeNode(@RequestBody @Valid MenuSave request) {
        service.saveMenu(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{menuNo}")
    public ResponseEntity changeMenuTreeNode(@PathVariable("menuNo") Long menuNo, @RequestBody @Valid MenuChange request) {
        service.changeMenu(menuNo, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{menuNo}")
    public ResponseEntity deleteMenuTreeNode(@PathVariable("menuNo") Long menuNo) {
        service.deleteMenu(menuNo);
        return ResponseEntity.ok().build();
    }
}
