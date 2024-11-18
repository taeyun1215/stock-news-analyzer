package com.devty.stocknewsanalyzer.collection.controller.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FetchNewsRequest {
    private String language = "en"; // 기본값을 'en'으로 설정
    private LocalDateTime from;
}
