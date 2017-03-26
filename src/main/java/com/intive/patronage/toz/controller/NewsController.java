package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.error.ErrorResponse;
import com.intive.patronage.toz.error.ValidationErrorResponse;
import com.intive.patronage.toz.model.constant.ApiUrl;
import com.intive.patronage.toz.model.view.NewsView;
import com.intive.patronage.toz.service.NewsService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Api(description = "News operations.")
@RestController
@RequestMapping(value = ApiUrl.NEWS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
class NewsController {
    private final NewsService newsService;

    @Autowired
    NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @ApiOperation(value = "Get all news.", responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 422, message = "Unprocessable entity.", response = ErrorResponse.class)
    })
    @GetMapping
    public List<NewsView> getAllNews(@ApiParam(allowableValues = "ARCHIVED, RELEASED, UNRELEASED")
                                         @RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "shortened", required = false, defaultValue = "false")
                                             Boolean shortened) {
        if (type != null) {
            return newsService.findAllNewsByType(type, shortened);
        }
        return newsService.findAllNews(shortened);
    }

    @ApiOperation(value = "Get news by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "News not found.", response = ErrorResponse.class),
    })
    @GetMapping(value = "/{id}")
    public NewsView getNewsById(@PathVariable UUID id) {
        return newsService.findById(id);
    }

    @ApiOperation(value = "Create news.", response = NewsView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request.", response = ValidationErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewsView> createNews(@Valid @RequestBody NewsView newsView) {
        NewsView createdNews = newsService.createNews(newsView);
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(baseLocation)
                .body(createdNews);
    }

    @ApiOperation("Delete news.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "News not found.", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<NewsView> deleteNewsById(@PathVariable UUID id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Update news.", response = NewsView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "News not found.", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Bad request.", response = ValidationErrorResponse.class)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public NewsView updateNews(@PathVariable UUID id, @Valid @RequestBody NewsView newsView) {
        return newsService.updateNews(id, newsView);
    }
}
