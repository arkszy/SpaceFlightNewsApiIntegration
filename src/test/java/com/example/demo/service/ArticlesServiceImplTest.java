package com.example.demo.service;

import com.example.demo.dto.ArticleDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArticlesServiceImplTest {


    private static MockWebServer mockBackEnd;
    private static ObjectMapper objectMapper;
    private ArticlesService articlesService;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
        objectMapper = null;
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockBackEnd.getPort());
        articlesService = new ArticlesServiceImpl(baseUrl);
    }

    @Test
    void givenRequestParamsAndJsonResponse_whenGetArticles_thenArticlesShouldHaveCorrectSize() throws Exception {
        //given
        String newsSiteParam = StringUtils.EMPTY;
        String titleParam = StringUtils.EMPTY;
        ArticleDTO article = new ArticleDTO("test11", "test12");
        ArticleDTO article2 = new ArticleDTO("test21", "test22");
        List<ArticleDTO> articleDTOList = Arrays.asList(new ArticleDTO[]{article, article2});
        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(articleDTOList))
                .addHeader("Content-Type", "application/json"));

        //when
        List<ArticleDTO> articleDTOS = articlesService.getArticles(newsSiteParam, titleParam);
        //then
        assertEquals(2, articleDTOS.size());
    }
}