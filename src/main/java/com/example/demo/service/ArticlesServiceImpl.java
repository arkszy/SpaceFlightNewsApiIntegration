package com.example.demo.service;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.exception.RestException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Service
public class ArticlesServiceImpl implements ArticlesService {

    private final WebClient webClient;

    public ArticlesServiceImpl(@Value("${spaceFlightNewsApi.baseUrl}") String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    @Override
    public List<ArticleDTO> getArticles(String newsSite, String title) throws RestException {
        try {
            ResponseEntity<List<ArticleDTO>> responseEntity = webClient.get().uri("v3/articles?_limit=10").accept(MediaType.APPLICATION_JSON).retrieve().toEntityList(ArticleDTO.class).block();
            if (responseEntity != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                List<ArticleDTO> articles = responseEntity.getBody();
                if (!CollectionUtils.isEmpty(articles)) {
                    Predicate<ArticleDTO> predicate = ArticlesPredicateStrategy.determinePredicate(newsSite, title);
                    if (predicate != null) {
                        return articles.stream().filter(predicate).toList();
                    } else {
                        return articles;
                    }
                }
            }
            return Collections.emptyList();
        } catch (WebClientResponseException ex) {
            throw new RestException(ex.getRawStatusCode(), ex.getMessage());
        }
    }

    @Override
    public List<ArticleDTO> getArticlesRecover(RestException e, String newsSite, String title) {
        log.debug("Trying retry {} with newSite: {} and title {}", e, newsSite, title);
        try {
            return getArticles(newsSite, title);
        } catch (Exception ex) {
            log.error("recover failed", ex);
            throw new UnsupportedOperationException("recover failed");
        }
    }
}