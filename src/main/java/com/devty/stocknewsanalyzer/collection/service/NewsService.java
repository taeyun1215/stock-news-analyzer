package com.devty.stocknewsanalyzer.collection.service;

import com.devty.stocknewsanalyzer.collection.entity.Article;
import com.devty.stocknewsanalyzer.collection.repository.ArticleRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    @Value("${newsapi.key}")
    private String apiKey;

    @Value("${newsapi.url}")
    private String apiUrl;

    private final ArticleRepository articleRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 최신 뉴스를 수집하여 반환합니다.
     * @param language 기사 언어 (예: en)
     * @param from 수집 기준 시간 (ISO 8601 형식)
     * @return 수집된 뉴스 기사 목록
     */
    public List<Article> fetchLatestArticles(String language, LocalDateTime from) {
        // OffsetDateTime을 ISO 8601 형식으로 포맷 (예: "2024-11-26T15:20:00Z")
        String fromFormatted = from.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        String query = "stock"; // 고정된 쿼리

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("language", language)
                .queryParam("pageSize", 10)
                .queryParam("from", fromFormatted)
                .queryParam("q", query)
                .queryParam("sortBy", "publishedAt")
                .queryParam("apiKey", apiKey);

        String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
        List<Article> articles = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(response);
            if (root.path("status").asText().equals("ok")) {
                JsonNode articlesNode = root.path("articles");
                for (JsonNode node : articlesNode) {
                    Article article = new Article();
                    article.setAuthor(node.path("author").asText(null));
                    article.setTitle(node.path("title").asText(null));
                    article.setDescription(node.path("description").asText(null));
                    article.setUrl(node.path("url").asText(null));
                    article.setUrlToImage(node.path("urlToImage").asText(null));

                    // 'publishedAt'을 LocalDateTime으로 파싱
                    String publishedAtStr = node.path("publishedAt").asText(null);
                    if (publishedAtStr != null) {
                        OffsetDateTime offsetDateTime = OffsetDateTime.parse(publishedAtStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        LocalDateTime publishedAt = offsetDateTime.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
                        article.setPublishedAt(publishedAt);
                    }

                    article.setContent(node.path("content").asText(null));
                    article.setSourceName(node.path("source").path("name").asText(null));
                    articles.add(article);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;
    }

    /**
     * 수집된 기사를 MongoDB에 저장합니다.
     * @param articles 저장할 기사 목록
     * @return 저장된 기사 수
     */
    public int saveArticles(List<Article> articles) {
        int savedCount = 0;
        for (Article article : articles) {
            if (!articleRepository.findByUrl(article.getUrl()).isPresent()) {
                articleRepository.save(article);
                savedCount++;
            }
        }
        return savedCount;
    }
}