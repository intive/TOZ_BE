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

import java.util.ArrayList;
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

    public List<NewsView> findAllNews(Boolean shortened) {
        List<NewsView> newsViews = new ArrayList<>();
        for (News news : newsRepository.findAll()) {
            createNewsList(shortened, newsViews, news);
        }
        return newsViews;
    }

    public List<NewsView> findAllNewsByType(String type, Boolean shortened) {
        if (EnumUtils.isValidEnum(News.Type.class, type)) {
            List<NewsView> newsViews = new ArrayList<>();
            for (News news : newsRepository.findByType(News.Type.valueOf(type))) {
                createNewsList(shortened, newsViews, news);
            }
            return newsViews;
        } else {
            throw new WrongEnumValueException(News.Type.class);
        }
    }

    public NewsView findById(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        return ModelMapper.convertToView(newsRepository.findOne(id), NewsView.class);
    }

    public NewsView createNews(final NewsView newsView) {
        News news = ModelMapper.convertToModel(newsView, News.class);
        setPublishedDate(news);
        return ModelMapper.convertToView(newsRepository.save(news), NewsView.class);
    }

    public void deleteNews(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        newsRepository.delete(id);
    }

    public NewsView updateNews(final UUID id, final NewsView newsView) {
        throwNotFoundExceptionIfNotExists(id);
        News news = ModelMapper.convertToModel(newsView, News.class);
        news.setId(id);
        news.setPublished(newsRepository.findOne(id).getPublished());
        setPublishedDate(news);
        return ModelMapper.convertToView(newsRepository.save(news), NewsView.class);
    }

    private void setPublishedDate(News news) {
        if (news.getType() == News.Type.RELEASED) {
            news.setPublished(new Date());
        }
    }

    private void createNewsList(Boolean shortened, List<NewsView> newsViews, News news) {
        NewsView newsView = ModelMapper.convertToView(news, NewsView.class);
        if (shortened == true) {
            newsView.setContents(new StringFormatter().
                    trimToLengthPreserveWord(newsView.getContents(),
                            NEWS_DESCRIPTION_LENGTH));
        }
        newsViews.add(newsView);
    }

    private void throwNotFoundExceptionIfNotExists(final UUID id) {
        if (!newsRepository.exists(id)) {
            throw new NotFoundException(NEWS);
        }
    }
}
