
package kware.apps.asp.bookmark;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cetus.service.CetusService;
import kware.apps.asp.bookmark.dto.request.CetusBookmarkToggle;
import kware.apps.asp.bookmark.dto.request.CetusSearchBookmark;
import kware.apps.asp.contents.dto.response.HomeData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CetusBookmarkService extends CetusService<CetusBookmark, CetusBookmarkDao> {

    @Transactional(readOnly = true)
    public List<CetusBookmark> getList(CetusSearchBookmark searchBookmark) {

        List<CetusBookmark> bookmarkList = dao.findListByUserUid(searchBookmark);
        try {
            ClassPathResource resource = new ClassPathResource("static/assets/data/3xmeta/list_data.json");
            byte[] jsonData = Files.readAllBytes(resource.getFile().toPath());

            ObjectMapper objectMapper = new ObjectMapper();
            List<HomeData> dataList = objectMapper.readValue(jsonData, new TypeReference<List<HomeData>>() {});

            Map<Long, HomeData> dataMap = dataList.stream()
                                        .collect(Collectors.toMap(HomeData::getUid, data -> data));

            for (CetusBookmark bookmark : bookmarkList) {
                HomeData data = dataMap.get(bookmark.getContentsUid());
                if (data != null) {
                    bookmark.setTitle(data.getTitle());
                    bookmark.setDescription(data.getDescription());
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            // model.addAttribute("homeDataList", null);
        }

        return bookmarkList;
    }

    @Transactional
    public Boolean toggleLike(CetusBookmarkToggle bean, Long userUid) {

        CetusBookmark bookmark = new CetusBookmark( userUid, bean.getContentsUid() );

        bookmark.setRegUid(userUid);
        bookmark.setUpdtUid(userUid);

        // 코드 체크
        // vaildationTargetType(wish.getTargetType());

        // 기존 좋아요 체크
        Boolean wishExists = dao.isBookmarkExists(bookmark);

        if(wishExists) {
             dao.delete(bookmark);
             return false;
        } else {
             dao.insert(bookmark);
             return true;
        }
    }

    @Transactional(readOnly = true)
    public Boolean isBookmarkExists(CetusBookmark bookmark) {
        return dao.isBookmarkExists(bookmark);
    }

    // private void vaildationTargetType(String targetType) {
    //     Map<String, CetusCode> wishType =
    //             codeService.findByCode("WISH_TYPE").stream()
    //                     .collect(
    //                             Collectors.toMap(
    //                                     CetusCode::getCode , cetusCode -> cetusCode)
    //                     );

    //     if(!wishType.containsKey(targetType)) {
    //         throw new IllegalArgumentException("target Type is not valid");
    //     }
    // }

}
