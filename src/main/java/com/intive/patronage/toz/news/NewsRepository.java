package com.intive.patronage.toz.news;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.news.model.db.News;

import java.util.List;

public interface NewsRepository extends IdentifiableRepository<News> {
    List<News> findByType(News.Type type);
    List<News> findByTypeOrderByCreatedDesc(News.Type type);
    List<News> findAllByOrderByCreatedDesc();
}
