package kware.common.config.csv;

import cetus.annotation.CSVColumn;
import cetus.util.DateTimeUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class CSVRender {

    // private static final String PREFIX = "myData";
    private static final String SUFFIX = ".zip";

    private Integer perSize;            // 1csv : data 개수
    private File tempFile;
    private Path tempDir;
    private Class<?> clazz;
    private List<String> header;
    private List<Field> fields;
    private Object[] body;

    /*
    *  생성자
    *  - persize : 1csv file - data 개수
    *  - CSVColumn 붙은 column 불러오기
    */
    public CSVRender(Class<?> clazz , Integer perSize, String downloadPath) throws IOException {
        this.clazz = clazz;
        this.perSize = perSize;

        this.makeDirFile(downloadPath);         // tempFile, tempDir create
        this.getHeader();           // clazz 통해 header -> CSVColumn column get
    }

    /*
    *  makeDirFile
    *  - 임시 파일, dir 생성
    */
    public void makeDirFile(String downloadPath) throws IOException{

        String PREFIX = UUID.randomUUID().toString();
        this.createMkdir(downloadPath +  File.separator + DateTimeUtil.getTodayShort());                 // 해당 경로 file mkdir
        Path dir = Paths.get(String.format("%s%s%s%s%s", downloadPath, File.separator, DateTimeUtil.getTodayShort(), File.separator, PREFIX));
        Path file = Paths.get(String.format("%s%s%s%s%s", downloadPath, File.separator, DateTimeUtil.getTodayShort(), File.separator, PREFIX + SUFFIX));
        String separator = System.getProperty("file.separator");
        new File(String.format("%s%s%s%s%s", downloadPath, File.separator, DateTimeUtil.getTodayShort(), File.separator, PREFIX + SUFFIX));
        this.tempFile = Files.createFile(file).toFile();
        this.tempDir = Files.createDirectories(dir);
    }


    /*
    *  getHeader
    *  - header : header 한글명
    *  - fields : 해당 header column
    */
    public void getHeader(){

        // ** -> header
        this.header = new ArrayList<>();
        this.fields = new ArrayList<>();

        Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(CSVColumn.class))
                .forEachOrdered(f -> {
                    CSVColumn cc = f.getAnnotation(CSVColumn.class);
                    this.header.add(cc.headerName());
                    this.fields.add(f);
                });
    }

    /*
    *  renderCSV
    *  - 해당 page의 list를 불러와 header, body에 write
    *  - 1 csv : 1header + (perSize)body
    */
    public void renderCSV(List<?> list, Integer num) throws IOException{

        File pageFile = new File(tempDir.toFile(), String.format("page-%02d.csv", num));       // param1 : The parent abstract pathname
                                                                                                    // param2 : The child pathname string
        writeHeader(pageFile, list);
    }


    /*
    *  writeHeader
    *  - write header
    *  - body write method 호출
    */
    public void writeHeader(File pageFile, List<?> list) throws IOException{
            // CSVPrineter - param1 : appendable stream to "which to print" (Must not be null)
            //             - param2 : format the CSV format (Must not be null)
            try (CSVPrinter printer = new CSVPrinter(new FileWriter(pageFile), CSVFormat.DEFAULT)) {

                // header
                printer.printRecord(this.header);

                // body
                writeBody(list, printer);

            }
    }

    /*
    *  writeBody
    *  - body write
    *  -> getter을 통해 data get
    */
    public void writeBody(List<?> list, CSVPrinter printer) throws IOException{

        for (Object bean : list) {

            this.body = new Object[fields.size()];
            int idx = 0;


            for(Field field : fields){

                try{

                    // get method를 통해 해당 column data get
                    Method method = clazz.getMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                    Object obj = method.invoke(bean);

                    body[idx++] = obj;

                }catch(Exception e){}
            }

            printer.printRecord(body);
        }
        printer.flush();
        printer.close();
        list.clear();
    }

    /*
    *  ZipCsvFiles
    *  - [header + body].csv file 전체 zip (temp)
    */
    public File ZipCsvFiles() throws IOException {

        // Create a zip archive of the CSV files
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(tempFile))) {
            for (File pageFile : tempDir.toFile().listFiles()) {
                try (FileInputStream fileIn = new FileInputStream(pageFile)) {
                    ZipEntry zipEntry = new ZipEntry(pageFile.getName());
                    zipOut.putNextEntry(zipEntry);

                    IOUtils.copy(fileIn, zipOut);

                    zipOut.closeEntry();
                }
            }
        }

        // Delete the temporary CSV files
        FileUtils.deleteDirectory(tempDir.toFile());

        return this.tempFile;
    }

    /*
    *  createMkdir
    *  - 파일 경로 없을 경우 mkdirs
    *  -> mkdirs : 해당 경로에 있는 모든 폴더에 대해 없으면 생성
    */
    private String createMkdir(String downloadUrls) {
		File folder = new File(downloadUrls);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	    return downloadUrls;
	}
}
