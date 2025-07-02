package kware.apps.asp.contents.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cetus.user.UserUtil;
import kware.apps.asp.bookmark.CetusBookmark;
import kware.apps.asp.bookmark.CetusBookmarkService;
import kware.apps.asp.contents.dto.request.CommentsSearch;
import kware.apps.asp.contents.dto.response.CommentsPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cetus.bean.Page;
import cetus.bean.Pageable;
import kware.apps.asp.contents.domain.CetusCategories;
import kware.apps.asp.contents.domain.CetusContents;
import kware.apps.asp.contents.domain.CetusContentsComment;
import kware.apps.asp.contents.domain.CetusContentsDao;
import kware.apps.asp.contents.domain.CetusTags;
import kware.apps.asp.contents.dto.request.ContentsSearch;
import kware.apps.asp.contents.dto.response.ContentsPage;
import kware.apps.asp.contents.dto.response.ContentsView;
import kware.apps.asp.contents.dto.request.ContentChange;
import kware.apps.manager.cetus.contents.categories.Categories;
import kware.apps.manager.cetus.contents.categories.Sources;
import kware.apps.manager.cetus.contents.categories.Types;
import kware.common.file.service.CommonFileService;
import kware.common.file.tus.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CetusContentsService {

    private final CetusContentsDao dao;
    private final CommonFileService commonFileService;
    private final CetusBookmarkService bookmarkService;

    public List<CetusCategories> categoriesList() {
        return List.of(
            new CetusCategories(Categories.FIELD, Categories.FIELDNAME, Categories.ITEMS),
            // new CetusCategories(ContentFormat.FIELD, ContentFormat.FIELDNAME, ContentFormat.ITEMS),
            // new CetusCategories(ContentRatings.FIELD, ContentRatings.FIELDNAME, ContentRatings.ITEMS),
            // new CetusCategories(DateRange.FIELD, DateRange.FIELDNAME, DateRange.ITEMS),
            // new CetusCategories(Language.FIELD, Language.FIELDNAME, Language.ITEMS),
            // new CetusCategories(Regions.FIELD, Regions.FIELDNAME, Regions.ITEMS),
            // new CetusCategories(Sns.FIELD, Sns.FIELDNAME, Sns.ITEMS),
            new CetusCategories(Sources.FIELD, Sources.FIELDNAME, Sources.ITEMS),
            new CetusCategories(Types.FIELD, Types.FIELDNAME, Types.ITEMS)
        );
    }

    @Transactional(readOnly = true)
    public Page<ContentsPage> getContentPageList(ContentsSearch search, Pageable pageable) {
        return dao.page("contentPageList", "contentPageListCount", search, pageable);
    }

    @Transactional(readOnly = true)
    public ContentsView view(Long uid) {
        ContentsView content = dao.contentsView(uid);
        List<CetusTags> tags = dao.findTagsByContentsUid(uid);
        content.setTags(tags);

        CommentsSearch commentsSearch = new CommentsSearch(UserUtil.getUserWorkplaceUid(), uid);
        Integer ratingAvg = dao.commentRatingAvg(commentsSearch);
        content.setRatings(ratingAvg);

        Boolean exists = bookmarkService.isBookmarkExists(new CetusBookmark(UserUtil.getUser().getUid(), uid));
        content.setBookmark((exists) ? "Y" : "N");

        if(content.getFilePath() != null) {
            content.setFilePath(EncryptionUtil.encrypt(content.getFilePath()));
        }
        if(content.getFileName() != null) {
            content.setFileName(EncryptionUtil.encrypt(content.getFileName()));
        }

        return content;
    }

    @Transactional
    public void changeContent(Long uid, ContentChange request) {
        ContentsView view = dao.contentsView(uid);

        // img
        Long contentFile = commonFileService.processFileSeparately(request.getContentFile(), request.getContentFileDel(), request.getContentFileUid());
        Long thumbnail = commonFileService.processFileSeparately(request.getThumbnail(), request.getThumbnailDel(), request.getThumbnailUid());

        ContentsView bean = view.changeContent(uid, request);
        bean.setFileUids(contentFile, thumbnail);

        dao.contentsUpdate(bean);
    }

    @Transactional
    public void insertComment(CetusContentsComment comment) {
        comment.setUser(UserUtil.getUser().getUid());
        dao.insertComment(comment);
    }

    @Transactional(readOnly = true)
    public List<CetusContentsComment> listComments(Long contentsUid) {
        return dao.listComments(contentsUid);
    }

    @Transactional(readOnly = true)
    public Page<CommentsPage> findAllCommentsPage(CommentsSearch search, Pageable pageable) {
        search.setWorkplaceUid(UserUtil.getUserWorkplaceUid());
        return dao.page("commentsPage", "commentsCount", search, pageable);
    }

    @Transactional(readOnly = true)
    public Map<String, Integer> findAllCommentCntByType(CommentsSearch search) {
        search.setWorkplaceUid(UserUtil.getUserWorkplaceUid());
        search.setType("OPINION");
        Integer opinion = dao.findCntByType(search);

        search.setType("QUESTION");
        Integer question = dao.findCntByType(search);

        search.setType("REPORT");
        Integer report = dao.findCntByType(search);

        CommentsSearch commentsSearch = new CommentsSearch(UserUtil.getUserWorkplaceUid(), search.getContentsUid());
        Integer ratingAvg = dao.commentRatingAvg(commentsSearch);

        Map<String, Integer> map = new HashMap<>();
        map.put("total", (opinion + question + report));
        map.put("opinion", opinion);
        map.put("question", question);
        map.put("report", report);
        map.put("ratingAvg", ratingAvg);
        return map;
    }

}
