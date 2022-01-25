package com.example.demo.service;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.exception.RestException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticlesServiceImpl implements ArticlesService {

    public List<ArticleDTO> getArticles(String newsSite, String title) throws RestException {
        WebClient webClient = WebClient.create("https://api.spaceflightnewsapi.net/");

        try {
            ResponseEntity<List<ArticleDTO>> responseEntity = webClient.get().uri("v3/articles?_limit=10").accept(MediaType.APPLICATION_JSON).retrieve().toEntityList(ArticleDTO.class).block();
            if (responseEntity != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                List<ArticleDTO> articles = responseEntity.getBody();
                if (!CollectionUtils.isEmpty(articles)) {
                    Predicate<ArticleDTO> predicate = ArticlesPredicateStrategy.determinePredicate(newsSite, title);
                    if (predicate != null) {
                        return articles.stream().filter(predicate).collect(Collectors.toList());
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