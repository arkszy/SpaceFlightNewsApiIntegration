package com.example.demo.controller;


import com.example.demo.exception.RestException;
import com.example.demo.service.ArticlesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ArticlesController {

    private ArticlesService articlesService;

    public ArticlesController(ArticlesService articlesService) {
        this.articlesService = articlesService;
    }

    @GetMapping("/articles")
    public ResponseEntity getArticles(@RequestParam(required = false) String newsSite, @RequestParam(required = false) String keyWord) {
        try {
            return ResponseEntity.ok().body(articlesService.getArticles(newsSite, keyWord));
        } catch(RestException e){
            return ResponseEntity.status(HttpStatus.valueOf(e.getStatusCode())).body(e.getMessage());
        } catch(Exception e){
            return ResponseEntity.internalServerError().body(String.format("something went wrong %s", e));
        }
    }
}
