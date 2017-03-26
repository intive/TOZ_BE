package com.intive.patronage.toz.repository;

import com.intive.patronage.toz.model.db.News;

import java.util.List;

public interface NewsRepository extends IdentifiableRepository<News> {
    List<News> findByType(News.Type type);
}
