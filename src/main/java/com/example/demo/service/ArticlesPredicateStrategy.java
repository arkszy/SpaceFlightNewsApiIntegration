package com.example.demo.service;

import com.example.demo.dto.ArticleDTO;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

@UtilityClass
public class ArticlesPredicateStrategy {

    public Predicate<ArticleDTO> determinePredicate(String newsSite, String keyWord) {
        Predicate<ArticleDTO> predicate = null;
        if (StringUtils.isNotBlank(newsSite) && StringUtils.isBlank(keyWord)) {
            predicate = new Predicate<ArticleDTO>() {
                @Override
                public boolean test(ArticleDTO article) {
                    return newsSite.equalsIgnoreCase(article.getNewsSite());
                }
            };
        } else if (StringUtils.isBlank(newsSite) && StringUtils.isNotBlank(keyWord)) {
            predicate = new Predicate<ArticleDTO>() {
                @Override
                public boolean test(ArticleDTO article) {
                    return article.getTitle().contains(keyWord);
                }
            };
        } else if (StringUtils.isNotBlank(newsSite) && StringUtils.isNotBlank(keyWord)) {
            predicate = new Predicate<ArticleDTO>() {
                @Override
                public boolean test(ArticleDTO article) {
                    return newsSite.equalsIgnoreCase(article.getNewsSite()) && article.getTitle().contains(keyWord);
                }
            };
        }
        return predicate;
    }
}
