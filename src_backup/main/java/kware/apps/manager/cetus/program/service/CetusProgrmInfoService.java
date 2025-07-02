package kware.apps.manager.cetus.program.service;


import cetus.bean.Page;
import cetus.bean.Pageable;
import cetus.user.UserUtil;
import kware.apps.manager.cetus.program.domain.CetusProgrmInfo;
import kware.apps.manager.cetus.program.domain.CetusProgrmInfoDao;
import kware.apps.manager.cetus.program.dto.request.*;
import kware.apps.manager.cetus.program.dto.response.MenuProgrmInfoList;
import kware.apps.manager.cetus.program.dto.response.ProgrmFullInfo;
import kware.apps.manager.cetus.program.dto.response.ProgrmInfoList;
import kware.common.file.service.CommonFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CetusProgrmInfoService {

    private final CetusProgrmInfoDao dao;
    private final CommonFileService commonFileService;

    @Transactional(readOnly = true)
    public Page<MenuProgrmInfoList> findAllMenuProgramPage(MenuProgrmInfoSearch search, Pageable pageable) {
        search.setWorkplaceUid(UserUtil.getUserWorkplaceUid());
        return dao.page("menuProgramPageList", "menuProgramPageCount", search, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProgrmInfoList> findAllProgramPage(ProgrmInfoSearch search, Pageable pageable) {
        search.setWorkplaceUid(UserUtil.getUserWorkplaceUid());
        return dao.page("programPageList", "programPageCount", search, pageable);
    }

    @Transactional(readOnly = true)
    public CetusProgrmInfo view(Long uid) {
        return dao.view(uid);
    }

    @Transactional(readOnly = true)
    public CetusProgrmInfo findProgramByUrl(String url) {
        ProgrmFullInfoSearch fullInfoSearch = new ProgrmFullInfoSearch(url, UserUtil.getUserWorkplaceUid());
        return dao.getProgramByUrl(fullInfoSearch);
    }

    @Transactional(readOnly = true)
    public ProgrmFullInfo findProgramFullInfoByUrl(String url) {
        ProgrmFullInfoSearch fullInfoSearch = new ProgrmFullInfoSearch(url, UserUtil.getUserWorkplaceUid());
        return dao.getProgramFullInfoByUrl(fullInfoSearch);
    }

    @Transactional
    public void saveProgram(ProgramSave request) {

        // img
        Long leftSlideImg = commonFileService.processFileSeparately(request.getLeftImg(), null, null);
        Long rightSlideImg = commonFileService.processFileSeparately(request.getRightImg(), null, null);
        Long mainLogoImg = ("Y".equals(request.getIsRootUrl())) ? commonFileService.processFileSeparately(request.getLogoImg(), null, null) : null;
        Long companyImg = ("Y".equals(request.getIsRootUrl())) ? commonFileService.processFileSeparately(request.getCompanyImg(), null, null) : null;

        CetusProgrmInfo bean = new CetusProgrmInfo(request, UserUtil.getUserWorkplaceUid());
        bean.setImgUids(leftSlideImg, rightSlideImg, mainLogoImg, companyImg);

        dao.insert(bean);
    }

    @Transactional
    public void changeProgram(Long uid, ProgramChange request) {
        CetusProgrmInfo view = dao.view(uid);

        // img
        Long leftSlideImg = commonFileService.processFileSeparately(request.getLeftImg(), request.getLeftImgDel(), request.getLeftImgUid());
        Long rightSlideImg = commonFileService.processFileSeparately(request.getRightImg(), request.getRightImgDel(), request.getRightImgUid());
        Long mainLogoImg = ("Y".equals(request.getIsRootUrl())) ? commonFileService.processFileSeparately(request.getLogoImg(), request.getLogoImgDel(), request.getLogoImgUid()) : request.getLogoImgUid();
        Long companyImg = ("Y".equals(request.getIsRootUrl())) ? commonFileService.processFileSeparately(request.getCompanyImg(), request.getCompanyImgDel(), request.getCompanyImgUid()) : request.getCompanyImgUid();

        CetusProgrmInfo bean = view.changeProgram(uid, request);
        bean.setImgUids(leftSlideImg, rightSlideImg, mainLogoImg, companyImg);

        dao.update(bean);
    }

}
