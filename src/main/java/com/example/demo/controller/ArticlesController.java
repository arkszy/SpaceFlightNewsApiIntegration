package com.example.demo.controller;


import com.example.demo.dto.ArticleDTO;
import com.example.demo.exception.RestException;
import com.example.demo.service.ArticlesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ArticlesController {

    private final ArticlesService articlesService;

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDTO>> getArticles(@RequestParam(required = false) String newsSite, @RequestParam(required = false) String title) throws RestException, IOException, InterruptedException {
        return ResponseEntity.ok().body(articlesService.getArticles(newsSite, title));
    }
}
