package com.example.demo.service;

import com.example.demo.dto.ArticleDTO;
import com.example.demo.exception.RestException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

public interface ArticlesService {
   // @Retryable(value = RestException.class, maxAttempts = 3, backoff = @Backoff(value = 5000, delay = 5000))
    List<ArticleDTO> getArticles( String newsSite, String keyWord) throws IOException, InterruptedException, RestException;
    //@Recover
    List<ArticleDTO> getArticlesRecover(RestException e, String newsSite, String keyWord);
}