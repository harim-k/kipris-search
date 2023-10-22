package com.example.kiprissearch.domain.utils;

import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {


    public static final String WHITELIST_FILE_NAME = "whitelist.txt";
    public static final String API_KEY_FILE_NAME = "api-key.txt";
    public static final String DOWNLOAD_FILE_PATH = "src/main/resources/download/";


    public static void saveAsFile(Workbook workbook,
                                  String filename) throws IOException {
        // 변경된 내용을 파일에 저장
        FileOutputStream fos = new FileOutputStream(DOWNLOAD_FILE_PATH + filename);
        workbook.write(fos);
        fos.close();
    }

    @SneakyThrows
    public static void saveAsFile(MultipartFile file,
                                  String filename) {
        File uploadFile = new File(filename);

        // 파일을 저장
        try (OutputStream os = new FileOutputStream(uploadFile)) {
            os.write(file.getBytes());
        }
    }

    @SneakyThrows
    public static void downloadFile(HttpServletResponse response,
                                    String filename) {
        downloadFile(response,
                     DOWNLOAD_FILE_PATH + filename,
                     filename);
    }

    @SneakyThrows
    public static void downloadFile(HttpServletResponse response,
                                    String filePath,
                                    String filename) {
        // 파일명 한글 사용을 위해 인코딩
        String encodedFilename = URLEncoder.encode(filename, "UTF-8");

        // Set the content type and attachment header.

        response.setCharacterEncoding("UTF-8");
        response.addHeader(
                "Content-disposition",
                StringUtils.join("attachment;filename*=UTF-8''", encodedFilename));
        response.setContentType("application/ms-excel");

        InputStream inputStream = new FileInputStream(filePath);
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }

    public static String concatTimestamp(String filename) {
        return StringUtils.join(getTimeStampString(),
                                "_",
                                filename);
    }

    private static String getTimeStampString() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss"); // 날짜와 시간 포맷
        return currentTime.format(formatter);
    }

    private static void deleteFilesInDirectory(File directory) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 하위 디렉토리에 대해 재귀적으로 호출하여 파일 삭제
                    deleteFilesInDirectory(file);
                } else {
                    // 파일을 삭제
                    if (file.delete()) {
                        System.out.println("파일 삭제: " + file.getAbsolutePath());
                    } else {
                        System.out.println("파일 삭제 실패: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }

    public static void deleteFile(String filepath) {
        File file = new File(filepath);

        if (file.exists()) {
            file.delete();
        }
    }

    public static void deleteAll() {
        deleteFilesInDirectory(new File(DOWNLOAD_FILE_PATH));
    }


    @SneakyThrows
    public static List<String> read(MultipartFile file) {
        List<String> keywords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(keyword)) {
                    keywords.add(keyword);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return keywords;
    }
}
