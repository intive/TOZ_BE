package com.intive.patronage.toz.service;

import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.exception.WrongEnumValueException;
import com.intive.patronage.toz.model.ModelMapper;
import com.intive.patronage.toz.model.db.News;
import com.intive.patronage.toz.model.view.NewsView;
import com.intive.patronage.toz.repository.NewsRepository;
import com.intive.patronage.toz.util.StringFormatter;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {
    private final static String NEWS = "News";
    private final NewsRepository newsRepository;
    private static final Integer NEWS_DESCRIPTION_LENGTH = 100;

    @Autowired
    NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<News> findAllNews(String type, Boolean shortened) {
        List<News> newsList;
        if (type == null) {
            newsList = newsRepository.findAll();
        } else if (EnumUtils.isValidEnum(News.Type.class, type)) {
            newsList = newsRepository.findByType(News.Type.valueOf(type));
        } else {
            throw new WrongEnumValueException(News.Type.class);
        }
        return createShortenedNewsContents(shortened, newsList);
    }

    public News findById(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        return newsRepository.findOne(id);
    }

    public News createNews(final NewsView newsView) {
        News news = ModelMapper.convertToModel(newsView, News.class);
        setPublishedDate(news);
        return newsRepository.save(news);
    }

    public void deleteNews(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        newsRepository.delete(id);
    }

    public News updateNews(final UUID id, final NewsView newsView) {
        throwNotFoundExceptionIfNotExists(id);
        News news = ModelMapper.convertToModel(newsView, News.class);
        news.setId(id);
        news.setPublished(newsRepository.findOne(id).getPublished());
        setPublishedDate(news);
        return newsRepository.save(news);
    }

    private void setPublishedDate(News news) {
        if (news.getType() == News.Type.RELEASED) {
            news.setPublished(new Date());
        }
    }

    private List<News> createShortenedNewsContents(Boolean shortened, List<News> newsList) {
        if (shortened) {
            for (News news : newsList) {
                news.setContents(new StringFormatter().trimToLengthPreserveWord(news.getContents(), NEWS_DESCRIPTION_LENGTH));
            }
        }
        return newsList;
    }

    private void throwNotFoundExceptionIfNotExists(final UUID id) {
        if (!newsRepository.exists(id)) {
            throw new NotFoundException(NEWS);
        }
    }
}
