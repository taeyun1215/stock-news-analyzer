package com.devty.stocknewsanalyzer.collection.controller;

import com.devty.stocknewsanalyzer.collection.controller.request.FetchNewsRequest;
import com.devty.stocknewsanalyzer.collection.entity.Article;
import com.devty.stocknewsanalyzer.collection.repository.ArticleRepository;
import com.devty.stocknewsanalyzer.collection.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsController {

	private final NewsService newsService;

	private final ArticleRepository articleRepository;

	/**
	 * 클라이언트로부터 언어와 수집 기준 시간을 받아 뉴스 기사를 수집하고 MongoDB에 저장합니다.
	 * @param request FetchNewsRequest 객체
	 * @return 저장된 기사 수와 상태 메시지
	 */
	@PostMapping("/fetch")
	public ResponseEntity<String> fetchAndSaveNews(@RequestBody FetchNewsRequest request) {
		String language = (request.getLanguage() != null && !request.getLanguage().isEmpty()) ? request.getLanguage() : "en";
//		OffsetDateTime from = request.getFrom(); // LocalDateTime에서 OffsetDateTime으로 변경

		LocalDateTime from = request.getFrom(); // LocalDateTime으로 변경

		if (from == null) {
			return ResponseEntity.badRequest().body("수집 기준 시간(from)을 제공해야 합니다.");
		}

		List<Article> articles = newsService.fetchLatestArticles(language, from);
		if (articles.isEmpty()) {
			return ResponseEntity.ok("수집된 기사가 없습니다.");
		}
		int savedCount = newsService.saveArticles(articles);
		return ResponseEntity.ok("저장된 기사 수: " + savedCount);
	}

	/**
	 * 저장된 뉴스 기사 수를 반환하는 엔드포인트
	 * @return 저장된 기사 수
	 */
	@GetMapping("/count")
	public long getArticleCount() {
		return articleRepository.count();
	}

	/**
	 * 모든 저장된 뉴스 기사를 반환하는 엔드포인트
	 * @return 모든 기사 리스트
	 */
	@GetMapping("/all")
	public Iterable<Article> getAllArticles() {
		return articleRepository.findAll();
	}
}