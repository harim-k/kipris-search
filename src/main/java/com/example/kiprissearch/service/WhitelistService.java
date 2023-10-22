package com.example.kiprissearch.service;

import com.example.kiprissearch.domain.utils.FileUtil;
import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.example.kiprissearch.domain.utils.FileUtil.WHITELIST_FILE_NAME;

@Service
public class WhitelistService {

    private static final String whitelistFilePath = FileUtil.WHITELIST_FILE_NAME;
    private List<String> whitelist = new ArrayList<>();


    @PostConstruct
    public void initialize() {
        loadWhitelistFromFile();
    }

    public List<String> getWhitelist() {
        return whitelist;
    }

    public List<String> add(String keyword) {
        whitelist.add(keyword);
        addToFile(keyword);

        return whitelist;
    }

    public void update(MultipartFile file) {
        whitelist = FileUtil.read(file);
        FileUtil.saveAsFile(file, WHITELIST_FILE_NAME);
    }

    private void addToFile(String keyword) {
        try (FileWriter fileWriter = new FileWriter(whitelistFilePath, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(keyword); // 단어를 다음 줄에 추가
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWhitelistFromFile() {
        try {
            File file = new File(whitelistFilePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileReader fileReader = new FileReader(whitelistFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                whitelist.add(line);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        whitelist = new ArrayList<>();

        File file = new File(whitelistFilePath);
        // 파일이 존재하면 삭제 시도
        if (file.exists()) {
            file.delete();
        }
    }
}
