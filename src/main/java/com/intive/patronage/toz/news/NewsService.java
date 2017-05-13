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

    List<News> findAllNews(String type, Boolean shortened, Boolean ordered) {
        List<News> newsList;
        checkIfEnumIsValid(type);
        if (EnumUtils.isValidEnum(News.Type.class, type) && ordered) {
            newsList = newsRepository.findByTypeOrderByCreatedDesc(News.Type.valueOf(type));
        } else if (EnumUtils.isValidEnum(News.Type.class, type) && !ordered) {
            newsList = newsRepository.findByType(News.Type.valueOf(type));
        } else if (type == null && ordered) {
            newsList = newsRepository.findAllByOrderByCreatedDesc();
        } else {
            newsList = newsRepository.findAll();
        }
        return createShortenedNewsContents(shortened, newsList);
    }

    private void checkIfEnumIsValid(String state) {
        if (state != null && !EnumUtils.isValidEnum(News.Type.class, state)) {
            throw new WrongEnumValueException(News.Type.class);
        }
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
