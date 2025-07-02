package kware.apps.manager.cetus.editorupload.service;


import cetus.config.CetusConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EditorUploadService {

    private final CetusConfig cetusConfig;

    @Transactional
    public Map<String, Object> ckImageUpload(MultipartFile multipartFile){
        Map<String, Object> params = new HashMap<>();

        String fileRoot = cetusConfig.getCkeditorPath(); // 저장될 외부 파일 경로
        String originalFileName = multipartFile.getOriginalFilename(); // 오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 파일 확장자
        UUID uuid = UUID.randomUUID();
        String savedFileName = uuid + extension; // 저장될 파일 명

        File targetFile = new File(String.format("%s%s%s", fileRoot, File.separator, savedFileName));
        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile); // 파일 저장
            params.put("url", "/ckImage/" + savedFileName);
            params.put("responseCode", "success");
        }
        catch (IOException e) {
            FileUtils.deleteQuietly(targetFile); // 저장된 파일 삭제
            params.put("responseCode", "error");
            e.printStackTrace();
        }

        return params;
    }
}
