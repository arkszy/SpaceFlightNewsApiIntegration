package com.example.demo.service;

import com.example.demo.dto.ArticleDTO;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

@UtilityClass
public class ArticlesPredicateStrategy {

    public Predicate<ArticleDTO> determinePredicate(String newsSite, String title) {
        Predicate<ArticleDTO> predicate = null;
        if (StringUtils.isNotBlank(newsSite) && StringUtils.isBlank(title)) {
            predicate = article -> newsSite.equalsIgnoreCase(article.getNewsSite());
        } else if (StringUtils.isBlank(newsSite) && StringUtils.isNotBlank(title)) {
            predicate = article -> article.getTitle().contains(title);
        } else if (StringUtils.isNotBlank(newsSite) && StringUtils.isNotBlank(title)) {
            predicate = article -> newsSite.equalsIgnoreCase(article.getNewsSite()) && article.getTitle().contains(title);
        }
        return predicate;
    }
}
