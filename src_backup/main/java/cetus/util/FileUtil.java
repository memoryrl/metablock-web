package cetus.util;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.Arrays;

@UtilityClass
public class FileUtil extends jodd.io.FileUtil {
    
    /**
     * 파일 확장자
     * @param orgFileName
     * @return
     */
    public static String extension(String orgFileName) {
        if ( StringUtil.isEmpty(orgFileName) ) {
            return "";
        }
        
        int lastIndex = orgFileName.lastIndexOf(".");
        return orgFileName.substring(lastIndex + 1);
    }
    
    /**
     * 유효한 파일 시퀀스인지 확인
     * @param fileUid
     * @return
     */
    public static boolean isValidFileUid(Long fileUid) {
        return StringUtil.isNotEmpty(fileUid) && fileUid != -1L;
    }
    
    public String concatWebPath(String... path) {
        return StringUtil.joining("/", Arrays.asList(path));
    }
    
    public String concatFilePath(String... path) {
        return StringUtil.joining(File.separator, Arrays.asList(path));
    }
}
