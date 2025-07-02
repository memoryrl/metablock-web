package kware.apps.manager.cetus.editorupload.service;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping(value="/cetus/image/upload")
@RequiredArgsConstructor
public class EditorUploadRestController {

    private final EditorUploadService editorUploadService;

    @PostMapping(value="/uploadCkImageFile", produces="application/json")
    public Map<String, Object> uploadCkImageFile(@RequestParam("file") MultipartFile multipartFile) {
        return editorUploadService.ckImageUpload(multipartFile);
    }
}