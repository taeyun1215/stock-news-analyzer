package com.devty.stocknewsanalyzer.collection.repository;

import com.devty.stocknewsanalyzer.collection.entity.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {
    Optional<Article> findByUrl(String url);
}
