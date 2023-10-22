package com.example.kiprissearch.service;

import com.example.kiprissearch.controller.to.SearchResponse;
import com.example.kiprissearch.domain.utils.FileUtil;
import com.example.kiprissearch.domain.utils.SearchResult;
import com.example.kiprissearch.external.KiprisClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.kiprissearch.domain.utils.FileUtil.API_KEY_FILE_NAME;
import static java.lang.Thread.sleep;

@Service
@RequiredArgsConstructor
public class KiprisService {

    private static final String inProgressFilePath = "inProgressFiles.txt";
    private final KiprisClient kiprisClient;
    private final WhitelistService whitelistService;



    public SearchResponse searchLikeWithoutWhitelist(Map<String, Integer> keywords) {
        List<String> whitelist = whitelistService.getWhitelist();
        whitelist.stream().forEach(w -> keywords.remove(w));

        LocalDateTime startTime = LocalDateTime.now();
        System.out.println(StringUtils.join(startTime,
                                            "\n키워드 수 : ",
                                            keywords.size()));

        SearchResponse searchResponse = searchLike(keywords);

        LocalDateTime endTime = LocalDateTime.now();
        System.out.println(StringUtils.join(endTime,
                                            "\n걸린 시간 : ",
                                            Duration.between(startTime, endTime).getSeconds(),
                                            "초"));
        return searchResponse;
    }

    public String getDownloadFilename(String filename) {
        return FileUtil.concatTimestamp(filename);
    }

    private SearchResponse searchLike(Map<String, Integer> keywords) {
        SearchResponse searchResponse = new SearchResponse();
        List<String> keywordList = new ArrayList<>(keywords.keySet());

        int chunkSize = 49;
        for (int startIndex = 0; startIndex < keywordList.size(); startIndex += chunkSize) {
            List<String> keywordChunk = keywordList.subList(startIndex, Math.min(startIndex + chunkSize, keywordList.size()));
            try {
                searchLikeChunk(startIndex, keywordChunk, searchResponse);
            } catch (Exception e) {
//                System.out.println(startIndex + "번째 줄까지 완료했습니다.");
//                keywords.get()
                break;
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return searchResponse;
    }
    private SearchResponse searchLikeChunk(int startIndex, List<String> keywords,
                                           SearchResponse searchResponse) throws Exception {
        AtomicInteger index = new AtomicInteger(1);

        keywords.parallelStream()
                .forEach(keyword -> {
                    SearchResult searchResult = null;
                    try {
                        searchResult = kiprisClient.searchLike(keyword);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    index.getAndIncrement();
                    System.out.println(startIndex + index.get());
                    if (searchResult == SearchResult.EXACT_MATCH) {
                        searchResponse.addExactlyRegisteredKeyword(keyword);
                    } else if (searchResult == SearchResult.PARTIAL_MATCH) {
                        searchResponse.addPartiallyRegisteredKeyword(keyword);
                    }
                });

        return searchResponse;
    }

    public SearchResponse searchLike2(Set<String> keywords) {
        return kiprisClient.searchLike2(keywords);
    }

    public SearchResult searchLike(String keyword) {
        try {
            return kiprisClient.searchLike(keyword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        FileUtil.deleteAll();
    }

    public List<String> getCompletedFilenames() {
        File directory = new File(FileUtil.DOWNLOAD_FILE_PATH);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String[] filenames = directory.list();
        Arrays.sort(filenames, Collections.reverseOrder());

        return List.of(filenames);
    }

    public List<String> getInProgressFilenames() {
        List<String> inProgressFilenames = new ArrayList<>();

        try {
            File file = new File(inProgressFilePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileReader fileReader = new FileReader(inProgressFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                inProgressFilenames.add(line);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        inProgressFilenames.sort(Collections.reverseOrder());

        return inProgressFilenames;
    }

    public void writeInProgressFile(String filename) {
        try (FileWriter writer = new FileWriter(inProgressFilePath, true)) {
            writer.write(filename + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeInProgressFile(String filename) {
        // 파일 경로와 제거할 텍스트를 지정합니다.
        String textToRemove = filename;

        try {
            // 파일을 읽기 위한 BufferedReader 생성
            BufferedReader reader = new BufferedReader(new FileReader(inProgressFilePath));

            // 임시 파일을 생성하여 수정된 내용을 저장
            File tempFile = new File(inProgressFilePath + ".temp");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            // 파일을 한 줄씩 읽어서 특정 텍스트가 포함된 줄을 건너뜁니다.
            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.contains(textToRemove)) {
                    writer.write(currentLine);
                    writer.newLine();
                }
            }

            // 파일 스트림을 닫습니다.
            reader.close();
            writer.close();

            // 원래 파일을 삭제하고 임시 파일을 원래 파일 이름으로 변경합니다.
            File originalFile = new File(inProgressFilePath);
            if (originalFile.delete()) {
                tempFile.renameTo(originalFile);
                System.out.println("텍스트가 성공적으로 제거되었습니다.");
            } else {
                System.out.println("파일을 업데이트하는 데 문제가 발생했습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeApiKey(String apiKey) {
        FileUtil.deleteFile(API_KEY_FILE_NAME);

        try (FileWriter writer = new FileWriter(API_KEY_FILE_NAME)) {
            writer.write(apiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }

        kiprisClient.initialize();
    }

    public String getApiKey() {

        String apiKey = null;

        try {
            File file = new File(API_KEY_FILE_NAME);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileReader fileReader = new FileReader(API_KEY_FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();
            if (StringUtils.isNotBlank(line)) {
                apiKey = line.trim();
            }
            System.out.println(apiKey);

            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiKey;
    }
}
