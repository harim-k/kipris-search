package com.example.kiprissearch.external;

import com.example.kiprissearch.controller.to.SearchResponse;
import com.example.kiprissearch.domain.utils.FileUtil;
import com.example.kiprissearch.domain.utils.SearchResult;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class KiprisClient {

    // sample request
    // GET http://plus.kipris.or.kr/kipo-api/kipi/trademarkInfoSearchService/getWordSearch?searchString=롯데&searchRecentYear=0&ServiceKey=BtYjo7KmUCj1SylQeCZ5uAr5a7r=JifMdlEdNnH189E=
    // GET http://plus.kipris.or.kr/kipo-api/kipi/trademarkInfoSearchService/getWordSearch?searchString=가오나시&searchRecentYear=0&ServiceKey=BtYjo7KmUCj1SylQeCZ5uAr5a7r=JifMdlEdNnH189E=

    private static final String API_KEY_PATH = FileUtil.API_KEY_FILE_NAME;

    private final String host = "http://plus.kipris.or.kr";
    private final String exactMatchPath = "openapi/rest/trademarkInfoSearchService/trademarkNameMatchSearchInfo";
    private final String searchPath = "kipo-api/kipi/trademarkInfoSearchService/getWordSearch";
    private final int searchRecentYear = 0;
    private final RestTemplate restTemplate;
    private String accessKey;

    private static boolean isPartiallyMatched(String targetKeyword, Set<String> extractKeywords) {
        return extractKeywords.stream()
                .anyMatch(extractKeyword -> extractKeyword.contains(targetKeyword));
    }

    private static boolean isExactlyMatched(String keyword, Set<String> extractKeywords) {
        return extractKeywords.contains(keyword);
    }

    @PostConstruct
    public void initialize() {
        loadApiKeyFromFile();
    }

    private void loadApiKeyFromFile() {
        try {
            File file = new File(API_KEY_PATH);

            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            FileReader fileReader = new FileReader(API_KEY_PATH);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();
            if (StringUtils.isNotBlank(line)) {
                accessKey = line.trim();
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean searchExact(String keyword) {
        URI uri = UriComponentsBuilder.fromUriString(host).path(exactMatchPath).queryParam("trademarkNameMatch", keyword).queryParam("accessKey", accessKey).build().encode(StandardCharsets.UTF_8) // https://github.com/heowc/programming-study/issues/66
                .toUri();

        String response = null;
        try {
            response = request(uri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return isExist(response, keyword);
    }

    public SearchResult searchLike(String keyword) throws Exception {
        URI uri = UriComponentsBuilder.fromUriString(host).path(searchPath).queryParam("searchString", keyword).queryParam("searchRecentYear", searchRecentYear).queryParam("ServiceKey", accessKey).build().encode(StandardCharsets.UTF_8) // https://github.com/heowc/programming-study/issues/66
                .toUri();

        String response = request(uri);
        Set<String> extractKeywords = extractKeyword(response);

        if ("스크랩앨범".equals(keyword)) {
            throw new Exception();
        }

        if (isExactlyMatched(keyword, extractKeywords)) {
            return SearchResult.EXACT_MATCH;
        } else if (isPartiallyMatched(keyword, extractKeywords)) {
            return SearchResult.PARTIAL_MATCH;
        } else {
            return SearchResult.NO_MATCH;
        }
    }

    private Set<String> extractKeyword(String xml) {
        Set<String> titles = new HashSet<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

            NodeList itemList = document.getElementsByTagName("item");
            for (int i = 0; i < itemList.getLength(); i++) {
                Element item = (Element) itemList.item(i);
                Element applicationStatus = (Element) item.getElementsByTagName("applicationStatus").item(0);
                Element title = (Element) item.getElementsByTagName("title").item(0);

                // 등록된 상표만 가져옵니다.
                if (!StringUtils.equals(applicationStatus.getTextContent(), "등록")) {
                    continue;
                }

                titles.add(title.getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return titles;
    }


    private boolean isExist(String response, String keyword) {
        return response.contains("<Title>" + keyword + "</Title>");
    }

    private String request(URI uri) throws Exception {
        ResponseEntity<String> response = null;

        try {
            response = restTemplate.getForEntity(uri, String.class);
        } catch (Exception e) {
            throw e;
        }

        return response.getBody();
    }

    public SearchResponse searchLike2(Set<String> keywords) {
        return null;
    }
}
