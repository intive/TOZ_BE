package com.intive.patronage.toz.news;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.error.exception.WrongEnumValueException;
import com.intive.patronage.toz.news.model.db.News;
import com.intive.patronage.toz.util.StringFormatter;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
class NewsService {
    private final static String NEWS = "News";
    private final NewsRepository newsRepository;
    private static final Integer NEWS_DESCRIPTION_LENGTH = 100;

    @Autowired
    NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    List<News> findAllNews(String type, Boolean shortened) {
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

    News findById(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        return newsRepository.findOne(id);
    }

    News createNews(final News news) {
        if (news.getType() == News.Type.RELEASED) {
            news.setPublished(new Date());
        } else {
            news.setPublished(null);
        }
        return newsRepository.save(news);
    }

    void deleteNews(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        newsRepository.delete(id);
    }

    News updateNews(final UUID id, final News news) {
        throwNotFoundExceptionIfNotExists(id);
        news.setId(id);
        Date published = newsRepository.findOne(id).getPublished();
        if (news.getType() == News.Type.RELEASED && published == null) {
            news.setPublished(new Date());
        } else {
            news.setPublished(published);
        }
        return newsRepository.save(news);
    }

    private List<News> createShortenedNewsContents(Boolean shortened, List<News> newsList) {
        if (shortened) {
            for (News news : newsList) {
                news.setContents(StringFormatter.
                        trimToLengthPreserveWord(news.getContents(), NEWS_DESCRIPTION_LENGTH));
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
