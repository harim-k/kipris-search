package com.example.kiprissearch.domain.utils;

import com.example.kiprissearch.controller.to.SearchResponse;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

public class ExcelUtil {

    @SneakyThrows
    public static String markKeywordsAndSaveAsFile(MultipartFile file,
                                                   SearchResponse searchResponse) {
        Workbook workbook = ExcelUtil.markKeywords(
                file,
                searchResponse.getExactlyRegisteredKeywords(),
                searchResponse.getPartiallyRegisteredKeywords());

        String filename = FileUtil.concatTimestamp(file.getOriginalFilename());
        FileUtil.saveAsFile(workbook, filename);

        return filename;
    }

    @SneakyThrows
    private static Workbook markKeywords(MultipartFile file,
                                         Set<String> exactlyRegisteredKeywords,
                                         Set<String> partiallyRegisteredKeywords) {
        // 엑셀 파일 열기
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0); // 시트 선택 (0은 첫 번째 시트)

        // 특정 열 번호 지정
        int columnIndexC = 2; // 예: 3번째 열 (0부터 시작)
        int columnIndexI = 8; // 예: 3번째 열 (0부터 시작)

        // 특정 열에서 단어 찾아서 하이라이트
        Iterator<Row> rowIterator = sheet.iterator();

        // 첫줄(헤더) 제외
        rowIterator.next();

        // C : 2, I : 8
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            markKeywords(exactlyRegisteredKeywords, partiallyRegisteredKeywords, row, columnIndexC, workbook);
            markKeywords(exactlyRegisteredKeywords, partiallyRegisteredKeywords, row, columnIndexI, workbook);
        }

        return workbook;
    }


    private static void markKeywords(Set<String> exactlyRegisteredKeywords,
                                     Set<String> partiallyRegisteredKeywords,
                                     Row row,
                                     int columnNumberToSearch,
                                     XSSFWorkbook workbook) {
        Cell cell = row.getCell(columnNumberToSearch);

        if (cell == null) {
            return;
        }

        // 특정 단어를 찾아서 노란색으로 하이라이트 설정
        cell.setCellType(CellType.STRING);
        String cellValue = cell.getStringCellValue();

        List<String> keywords = Arrays.asList(cellValue.split("[,\\s]+")); // 띄어쓰기 또는 쉼표로 단어 분리

        XSSFRichTextString richText = new XSSFRichTextString(cellValue);

        int startIndex;
        int endIndex = 0;

        for (String word : keywords) {
            XSSFFont font = workbook.createFont();

            if (exactlyRegisteredKeywords.contains(word)) {
                font.setColor(IndexedColors.RED.getIndex());

                startIndex = cellValue.indexOf(word, endIndex);
                endIndex = startIndex + word.length();

                richText.applyFont(startIndex, endIndex, font);
            }

            if (partiallyRegisteredKeywords.contains(word)) {
                font.setColor(IndexedColors.BLUE.getIndex());

                startIndex = cellValue.indexOf(word, endIndex);
                endIndex = startIndex + word.length();

                richText.applyFont(startIndex, endIndex, font);
            }
        }

        XSSFCellStyle style = workbook.createCellStyle();
        cell.setCellValue(richText);
        cell.setCellStyle(style);
    }

    @SneakyThrows
    public static Set<String> extractKeywords(MultipartFile file) {
        // 데이터를 저장할 리스트 생성
        Set<String> keywords = new HashSet<>();

        // 엑셀 파일 열기
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // 시트 선택 (0은 첫 번째 시트)

        // 특정 열 번호 지정
        int columnIndexC = 2; // 예: 3번째 열 (0부터 시작)
        int columnIndexI = 8; // 예: 3번째 열 (0부터 시작)

        // 특정 열에서 단어 추출
        Iterator<Row> rowIterator = sheet.iterator();

        // 첫줄(헤더) 제외
        rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            keywords.addAll(extractKeywords(row, columnIndexC));
            keywords.addAll(extractKeywords(row, columnIndexI));
        }

        return keywords;
    }

    @SneakyThrows
    public static Map<String, Integer> extractKeywordMap(MultipartFile file) {
        // 데이터를 저장할 리스트 생성
        Map<String, Integer> keywords = new HashMap<>();

        // 엑셀 파일 열기
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0); // 시트 선택 (0은 첫 번째 시트)

        // 특정 열 번호 지정
        int columnIndexC = 2; // 예: 3번째 열 (0부터 시작)
        int columnIndexI = 8; // 예: 3번째 열 (0부터 시작)

        // 특정 열에서 단어 추출
        Iterator<Row> rowIterator = sheet.iterator();

        // 첫줄(헤더) 제외
        rowIterator.next();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            keywords.putAll(extractKeywordMap(row, columnIndexC));
            keywords.putAll(extractKeywordMap(row, columnIndexI));
        }

        return keywords;
    }

    private static Set<String> extractKeywords(Row row, int columnCIndex) {
        Set<String> keywords = new HashSet<>();

        Cell cell = row.getCell(columnCIndex);
        if (cell != null) {
            cell.setCellType(CellType.STRING);
            String cellValue = cell.getStringCellValue();
            if (cellValue != null && !cellValue.isEmpty()) {
                // 콤마 또는 공백을 기준으로 단어 분할
                String[] words = cellValue.split("[,\\s]+");
                for (String word : words) {
                    keywords.add(word);
                }
            }
        }

        return keywords;
    }

    private static Map<String, Integer> extractKeywordMap(Row row, int columnCIndex) {
        Map<String, Integer> keywords = new HashMap<>();

        Cell cell = row.getCell(columnCIndex);
        if (cell != null) {
            cell.setCellType(CellType.STRING);
            String cellValue = cell.getStringCellValue();
            if (cellValue != null && !cellValue.isEmpty()) {
                // 콤마 또는 공백을 기준으로 단어 분할
                String[] words = cellValue.split("[,\\s]+");
                for (String word : words) {
                    keywords.put(word, row.getRowNum());
                }
            }
        }

        return keywords;
    }
}

