package com.intive.patronage.toz.service;

import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.exception.WrongEnumValueException;
import com.intive.patronage.toz.model.ModelMapper;
import com.intive.patronage.toz.model.db.News;
import com.intive.patronage.toz.model.view.NewsView;
import com.intive.patronage.toz.repository.NewsRepository;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    private final static String NEWS = "News";
    private final NewsRepository newsRepository;
    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    @Autowired
    NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<NewsView> findAllNews() {
        List<NewsView> newsViews = new ArrayList<>();
        for (News news : newsRepository.findAll()) {
            newsViews.add(MODEL_MAPPER.convertToView(news, NewsView.class));
        }
        return newsViews;
    }

    public List<NewsView> findAllNewsByType(String type) {
        if (EnumUtils.isValidEnum(News.Type.class, type)) {
            List<NewsView> newsViews = new ArrayList<>();
            for (News news : newsRepository.findByType(News.Type.valueOf(type))) {
                newsViews.add(MODEL_MAPPER.convertToView(news, NewsView.class));
            }
            return newsViews;
        } else {
            throw new WrongEnumValueException(News.Type.class);
        }
    }

    public NewsView findById(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        return MODEL_MAPPER.convertToView(newsRepository.findOne(id), NewsView.class);
    }

    public NewsView createNews(final NewsView newsView) {
        News news = MODEL_MAPPER.convertToModel(newsView, News.class);
        setPublishedDate(news);
        return MODEL_MAPPER.convertToView(newsRepository.save(news), NewsView.class);
    }

    public void deleteNews(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        newsRepository.delete(id);
    }

    public NewsView updateNews(final UUID id, final NewsView newsView) {
        throwNotFoundExceptionIfNotExists(id);
        News news = MODEL_MAPPER.convertToModel(newsView, News.class);
        news.setId(id);
        news.setPublished(newsRepository.findOne(id).getPublished());
        setPublishedDate(news);
        return MODEL_MAPPER.convertToView(newsRepository.save(news), NewsView.class);
    }

    private void setPublishedDate(News news) {
        if (news.getType() == News.Type.RELEASED) {
            news.setPublished(new Date());
        }
    }

    private void throwNotFoundExceptionIfNotExists(final UUID id) {
        if (!newsRepository.exists(id)) {
            throw new NotFoundException(NEWS);
        }
    }
}