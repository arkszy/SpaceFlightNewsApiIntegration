package com.example.demo.service;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.exception.RestException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ArticlesServiceImpl implements ArticlesService {

    public List<ArticleDTO> getArticles(String newsSite, String keyWord) throws IOException, InterruptedException, RestException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getArticlesRequest = HttpRequest.newBuilder(URI.create("https://api.spaceflightnewsapi.net/v3/articles?_limit=10")).GET().header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
        HttpResponse<String> response = client.send(getArticlesRequest, HttpResponse.BodyHandlers.ofString());
        if (HttpStatus.OK.value() == response.statusCode()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            List<ArticleDTO> articles = objectMapper.readValue(response.body(), new TypeReference<List<ArticleDTO>>() {
            });
            Predicate<ArticleDTO> predicate = ArticlesPredicateStrategy.determinePredicate(newsSite, keyWord);
            if (predicate != null) {
                return articles.stream().filter(predicate).collect(Collectors.toList());
            } else {
                return articles;
            }
        } else {
            throw new RestException(response.statusCode(), response.body());
        }
    }

    public List<ArticleDTO> getArticlesRecover(RestException e, String newsSite, String keyWord) {
        System.out.println(String.format("Trying retry %s with newSite: %s and keyWord %s", e, newsSite, keyWord));
        try {
            return getArticles(newsSite, keyWord);
        } catch(Exception ex){
            System.out.println(String.format("recoved failed %s", ex));
            throw new UnsupportedOperationException("recover failed");
        }
    }
}