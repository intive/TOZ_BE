package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.model.view.NewsView;
import com.intive.patronage.toz.service.NewsService;
import com.intive.patronage.toz.util.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/news", produces = MediaType.APPLICATION_JSON_VALUE)
class NewsController {
    private final NewsService newsService;
    private static final Integer NEWS_DESCRIPTION_LENGTH = 100;

    @Autowired
    NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public List<NewsView> getAllNews(@RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "shortened", required = false, defaultValue = "false")
                                             Boolean shortened) {
        List<NewsView> newsViews;
        if (type != null) {
            newsViews = newsService.findAllNewsByType(type);
        } else {
            newsViews = newsService.findAllNews();
        }
        if (shortened == true) {
            for (NewsView newsView : newsViews) {
                newsView.setContents(new StringFormatter().
                        cutStringAfterSpecifiedLength(newsView.getContents(), NEWS_DESCRIPTION_LENGTH));
            }
        }
        return newsViews;
    }

    @GetMapping(value = "/{id}")
    public NewsView getNewsById(@PathVariable UUID id) {
        return newsService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewsView> createNews(@Valid @RequestBody NewsView newsView) {
        NewsView createdNews = newsService.createNews(newsView);
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(baseLocation)
                .body(createdNews);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<NewsView> deleteNewsById(@PathVariable UUID id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public NewsView updateNews(@PathVariable UUID id, @Valid @RequestBody NewsView newsView) {
        return newsService.updateNews(id, newsView);
    }
}
