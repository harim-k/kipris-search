package com.example.kiprissearch.external;

import com.example.kiprissearch.controller.to.SearchResponse;
import com.example.kiprissearch.domain.utils.FileUtil;
import com.example.kiprissearch.domain.utils.SearchResult;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class KiprisClient2 {

    // sample request
    // GET http://plus.kipris.or.kr/kipo-api/kipi/trademarkInfoSearchService/getWordSearch?searchString=롯데&searchRecentYear=0&ServiceKey=6vdEotH5fpNYOUpCnGdhdNAYqNP1W1aYTII7=6dlZGY=
    // GET http://plus.kipris.or.kr/kipo-api/kipi/trademarkInfoSearchService/getWordSearch?searchString=가오나시&searchRecentYear=0&ServiceKey=6vdEotH5fpNYOUpCnGdhdNAYqNP1W1aYTII7=6dlZGY=
    private static final String API_KEY_PATH = FileUtil.API_KEY_FILE_NAME;

    private final String host = "http://plus.kipris.or.kr/";
    private final String exactMatchPath = "openapi/rest/trademarkInfoSearchService/trademarkNameMatchSearchInfo";
    private final String searchPath = "kipo-api/kipi/trademarkInfoSearchService/getWordSearch";
    private String accessKey;
    private final int searchRecentYear = 0;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://plus.kipris.or.kr")
            .build();

    private static boolean isPartiallyMatched(String targetKeyword, Set<String> extractKeywords) {
        return extractKeywords.stream().anyMatch(extractKeyword -> extractKeyword.contains(targetKeyword));
    }

    private static boolean isExactlyMatched(String keyword, Set<String> extractKeywords) {
        return extractKeywords.contains(keyword);
    }

    public SearchResult searchLike(String keyword) {
        // WebClient 빌더를 사용하여 WebClient 인스턴스를 생성합니다.
        URI uri = UriComponentsBuilder.fromUriString(host).path(searchPath).queryParam("searchString", keyword).queryParam("searchRecentYear", searchRecentYear).queryParam("ServiceKey", accessKey).build().encode(StandardCharsets.UTF_8) // https://github.com/heowc/programming-study/issues/66
                .toUri();

        // GET 요청을 보내고 응답을 받습니다.
        Mono<String> responseMono = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class);

        // 응답을 블로킹하고 결과를 문자열로 가져옵니다.
        String response = responseMono.block();

        Set<String> extractKeywords = extractKeyword(response);

        if (isExactlyMatched(keyword, extractKeywords)) {
            return SearchResult.EXACT_MATCH;
        } else if (isPartiallyMatched(keyword, extractKeywords)) {
            return SearchResult.PARTIAL_MATCH;
        } else {
            return SearchResult.NO_MATCH;
        }
    }

    public SearchResponse searchLike2(Set<String> keywords) {
        SearchResponse searchResponse = new SearchResponse();
        Set<String> results = new HashSet<>();

        List<String> responses = Flux.fromIterable(keywords)
                .flatMap(keyword -> callAPI(keyword))
                .parallel()
                .runOn(Schedulers.parallel()) // 병렬 처리를 위해 스레드 풀 사용
                .sequential()
                .collectList()
                .block();

        for (String response : responses) {
            results.addAll(extractKeyword(response));
        }


        for (String keyword : keywords) {
            if (isExactlyMatched(keyword, results)) {
                searchResponse.addExactlyRegisteredKeyword(keyword);
            } else if (isPartiallyMatched(keyword, results)) {
                searchResponse.addPartiallyRegisteredKeyword(keyword);
            }
        }

        return searchResponse;
        }


    private Mono<String> callAPI(String keyword) {
        Mono<String> responseMono = webClient.get()
                .uri("/kipo-api/kipi/trademarkInfoSearchService/getWordSearch?searchRecentYear=0&ServiceKey=6vdEotH5fpNYOUpCnGdhdNAYqNP1W1aYTII7=6dlZGY=&searchString={keyword}", keyword)
                .retrieve()
                .bodyToMono(String.class);
        return responseMono;
    }

    private Set<String> extractKeyword(String xml) {
        Set<String> titles = new HashSet<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));

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
}
