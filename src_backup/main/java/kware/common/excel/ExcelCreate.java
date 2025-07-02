package kware.common.excel;

import cetus.bean.Page;
import cetus.bean.Pageable;
import cetus.config.CetusConfig;
import cetus.util.HtmlUtil;
import kware.apps.manager.cetus.answer.dto.response.AnswerExcelList;
import kware.apps.manager.cetus.downloadshist.service.CetusDownloadsHistService;
import kware.apps.manager.cetus.enumstatus.DownloadTargetCd;
import kware.apps.manager.cetus.file.service.CetusFileService;
import kware.common.file.service.CommonFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelCreate {

    private final CommonFileService commonFileService;
    private final CetusDownloadsHistService downloadsHistService;
    private final CetusConfig cetusConfig;
    private final CetusFileService cetusFileService;

    public<T> void createExcelFile(
            Class<T> clazz,
            Function<Pageable, Page<T>> pageSupplier,
            DownloadTargetCd targetCd
    ) {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(formatter);
        String fileName = targetCd.getFileHeaderNm() + timestamp + ".xlsx";

        Long fileUid = commonFileService.generateUid();
        cetusFileService.saveFile(fileUid, fileName);
        downloadsHistService.saveDownloadsHistUser(targetCd.name(), fileUid);

        File file = null;

        try {
            ExcelRender excel = new ExcelRender(clazz);
            Pageable pageable = new Pageable(1000);
            Page<T> paging = pageSupplier.apply(pageable);

            for (int i = 0; i < paging.getTotalPages(); i++) {
                pageable.setPageNumber(i + 1);
                List<T> list = pageSupplier.apply(pageable).getList();
                excel.renderExcel(list);
            }

            file = excel.writeWorkBookToFile(cetusConfig.getDownloadPath(), fileName);
            cetusFileService.updateDownloadFile(fileUid, file);

        } catch (Exception e) { // {IOException}보다 넓게
            log.error("엑셀 생성 중 예외 발생 - fileUid: {}, msg: {}", fileUid, e.getMessage(), e);
            try {
                cetusFileService.deleteNotDownloadFile(fileUid);
                downloadsHistService.deleteDownloadHistUser(fileUid);
            } catch (Exception deleteEx) {
                log.error("파일 삭제 중 예외 발생 - fileUid: {}, msg: {}", fileUid, deleteEx.getMessage(), deleteEx);
            }
        }

    }


    public<T> void createExcelFileHtmlCustom(
            Class<T> clazz,
            Function<Pageable, Page<T>> pageSupplier,
            DownloadTargetCd targetCd
    ) {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(formatter);
        String fileName = targetCd.getFileHeaderNm() + timestamp + ".xlsx";

        Long fileUid = commonFileService.generateUid();
        cetusFileService.saveFile(fileUid, fileName);
        downloadsHistService.saveDownloadsHistUser(targetCd.name(), fileUid);

        File file = null;

        try {
            ExcelRender excel = new ExcelRender(clazz);
            Pageable pageable = new Pageable(1000);
            Page<T> paging = pageSupplier.apply(pageable);

            for (int i = 0; i < paging.getTotalPages(); i++) {
                pageable.setPageNumber(i + 1);
                List<T> list = pageSupplier.apply(pageable).getList();

                if (targetCd == DownloadTargetCd.USER_ANSWER) {
                    for (T item : list) {
                        if (item instanceof AnswerExcelList) {
                            AnswerExcelList answer = (AnswerExcelList) item;
                            answer.setAnswerCnt(
                                    HtmlUtil.stripHtmlPreserveLines(answer.getAnswerCnt())
                            );
                        }
                    }
                }
                excel.renderExcel(list);
            }

            file = excel.writeWorkBookToFile(cetusConfig.getDownloadPath(), fileName);
            cetusFileService.updateDownloadFile(fileUid, file);

        } catch (Exception e) { // {IOException}보다 넓게
            log.error("엑셀 생성 중 예외 발생 - fileUid: {}, msg: {}", fileUid, e.getMessage(), e);
            try {
                cetusFileService.deleteNotDownloadFile(fileUid);
                downloadsHistService.deleteDownloadHistUser(fileUid);
            } catch (Exception deleteEx) {
                log.error("파일 삭제 중 예외 발생 - fileUid: {}, msg: {}", fileUid, deleteEx.getMessage(), deleteEx);
            }
        }

    }

}
