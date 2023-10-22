package com.example.kiprissearch.controller.to;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class SearchResponse {
    private Set<String> exactlyRegisteredKeywords = new HashSet<>();
    private Set<String> partiallyRegisteredKeywords = new HashSet<>();

    public void addExactlyRegisteredKeyword(String keyword) {
        exactlyRegisteredKeywords.add(keyword);
    }

    public void addPartiallyRegisteredKeyword(String keyword) {
        partiallyRegisteredKeywords.add(keyword);
    }
}
