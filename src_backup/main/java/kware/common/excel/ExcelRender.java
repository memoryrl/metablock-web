package kware.common.excel;


import cetus.annotation.ExcelColumn;
import cetus.util.DateTimeUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
public class ExcelRender {
    private SXSSFWorkbook wb;
    private Row row;
    private Cell cell;
    private Class<?> clazz;
    private Sheet sheet;
    private Map<String, XSSFCellStyle> cellStyles;
    @Setter
    private int maxRowCanBeRendered = 50000;
    private int ROW_START_INDEX = 0;
    private List<Integer> nums;

    /*
     * excel 생성
     * */
    public ExcelRender(Class<?> clazz) {
        log.info("=======================START===========================");
        wb = new SXSSFWorkbook();
        wb.setCompressTempFiles(true); // 압축여부
        this.clazz = clazz; // @ExcelColumn이 가지고 있는 class
        DefaultExcelCellStyle cellStyle = new DefaultExcelCellStyle();
        cellStyles = cellStyle.setWb(wb); // [header, body, amount]style Map으로 받아옴
        nums = new ArrayList<>();
    }


    /*
     * excel 데이터 그리기
     */
    public void renderExcel(List<?> list) throws IOException {
        // list가 없으면 header만 sheet와 header만 그리고 반환
        if (list.isEmpty()) {
            createNewSheetWithHeader();
            return;
        }
        // sheet와 header 생성
        createNewSheetWithHeader();
        // 랜더링 된 데이터 cnt
        int renderedDataCnt = 0;
        // body에 cell 위치
        int rowIndex = ROW_START_INDEX + 1;
        // for문 돌아가면서 contents를 채움
        for (Object renderedData : list) {
            renderContent(renderedData, rowIndex++);
            renderedDataCnt++;
            // 시트당 최대값에 도달하게 되면 새로운 sheet 생성
            if (renderedDataCnt == maxRowCanBeRendered + 1) {
                log.info("rendering");
                // 초기화
                renderedDataCnt = 0;
                rowIndex = 1;
                // 새로운 sheet
                createNewSheetWithHeader();
            }
        }
        // sheet 캐쉬 비우기
        ((SXSSFSheet) sheet).flushRows(list.size());
        // list clear
        list.clear();
    }

    /*
      SXSSFWorkbook 쓰기(엑셀 다운로드)
     */
    public void writeWorkbook(HttpServletResponse res) throws IOException {
        OutputStream target = res.getOutputStream();
        wb.write(target);
        target.close();
        // 임시파일 삭제
        wb.dispose();
    }

    /* sheet 생성 및 header 만들어주는 메소드 호출 */
    public void createNewSheetWithHeader() {
        sheet = wb.createSheet();
        renderHeadersWithNewSheet(sheet, ROW_START_INDEX);
    }

    /* 헤더 생성 */
    protected void renderHeadersWithNewSheet(Sheet sheet, int rowIndex) {
        Row row = sheet.createRow(rowIndex);
        AtomicInteger counter = new AtomicInteger(0);
        this.nums.clear();
        Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ExcelColumn.class))
                .forEachOrdered(f -> {
                    ExcelColumn ec = f.getAnnotation(ExcelColumn.class);
                    cell = row.createCell(counter.getAndIncrement());
                    cell.setCellValue(ec.headerName());
                    cell.setCellStyle(cellStyles.get("headerStyle"));
                    this.nums.add(ec.headerName().length());
                });
    }

    protected void renderContent(Object data, int rowIndex) {
        row = sheet.createRow(rowIndex);
        AtomicInteger counter = new AtomicInteger(0);
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExcelColumn.class))
                .forEachOrdered(field -> {
                    cell = row.createCell(counter.get());
                    Object value = this.getValue(data, field);
                    this.maxColumnWidth(counter.get(), value);
                    counter.getAndIncrement();
                });
        this.updateColumnWidths();
    }
    private String getMethodName(Field field) {
        String methodName = field.getName();
        return "get" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
    }
    // obj value
    private Object getValue(Object data, Field field) {
        try {
            String methodName = getMethodName(field);
            MethodHandle methodHandle = MethodHandles.lookup()
                    .findVirtual(data.getClass(),methodName, MethodType.methodType(field.getType()));
            this.applyCellStyle(cell, methodHandle.invoke(data), methodName);
            return methodHandle.invoke(data);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

    // cellStyle 적용
    private void applyCellStyle(Cell cell, Object value, String column) {
        if (value instanceof Long && !column.toUpperCase().contains("UID")) {
            cell.setCellStyle(cellStyles.get("amountStyle"));
            cell.setCellValue(value == null ? 0 : Long.parseLong(value.toString()));
        } else {
            cell.setCellStyle(cellStyles.get("bodyStyle"));
            cell.setCellValue(value == null ? "" : value.toString());
        }
    }
    // 오토사이징
    private void updateColumnWidths() {
        IntStream.range(0, nums.size())
                .forEach(i -> sheet.setColumnWidth(i, (nums.get(i) * 256) + 1048));  // 변경된 부분: nums.get(i) * 255 -> nums.get(i) * 256
    }

    // length 최대값 구하기
    private void maxColumnWidth(int column, Object value) {
        int length = value == null ? 0 : value.toString().length();
        int newWidth = Math.max(nums.get(column), length);
        // 만약 새로운 너비가 150을 넘으면, 150로 제한
        if (newWidth > 150) {
            newWidth = 150;
        }
        nums.set(column, newWidth);
    }

    public File writeWorkBookToFile(String downloadPath, String fileName) throws IOException {
        // 날짜 디렉토리 생성 (예: 20250512)
        String todayDir = DateTimeUtil.getTodayShort();
        Path fullDirPath = Paths.get(downloadPath, todayDir);
        Files.createDirectories(fullDirPath); // 디렉토리 없으면 생성

        // 최종 파일 경로
        Path filePath = fullDirPath.resolve(fileName);

        // 엑셀 파일 저장
        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            wb.write(outputStream);
        } finally {
            wb.dispose(); // SXSSFWorkbook 내부 임시파일 삭제
        }

        log.info("Excel 파일이 저장되었습니다: {}", filePath.toAbsolutePath());

        // java.io.File 객체로 리턴
        return filePath.toFile();
    }
}
