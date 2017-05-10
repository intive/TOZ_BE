package com.intive.patronage.toz.news;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.news.model.db.News;
import com.intive.patronage.toz.news.model.view.NewsView;
import com.intive.patronage.toz.util.ModelMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Api(description = "News operations.")
@RestController
@RequestMapping(value = ApiUrl.NEWS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class NewsController {
    private final NewsService newsService;

    @Autowired
    NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @ApiOperation(value = "Get all news.", responseContainer = "List", notes =
            "Required roles: SA, TOZ, VOLUNTEER, ANONYMOUS for RELEASED type, or " +
                    "SA, TOZ for other types.")
    @ApiResponses(value = {
            @ApiResponse(code = 422, message = "Unprocessable entity.", response = ErrorResponse.class)
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ') or " +
            "(hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER', 'ANONYMOUS') and #type == 'RELEASED')")
    public ResponseEntity<List<NewsView>> getAllNews(
            @ApiParam(allowableValues = "ARCHIVED, RELEASED, UNRELEASED")
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "shortened", required = false, defaultValue = "false")
                    Boolean shortened) {
        final List<News> newsList = newsService.findAllNews(type, shortened);
        return ResponseEntity.ok().body(ModelMapper.convertToView(newsList, NewsView.class));
    }

    @ApiOperation(value = "Get news by id.", notes =
            "Required roles: SA, TOZ, VOLUNTEER, ANONYMOUS for RELEASED type, or " +
                    "SA, TOZ for other types.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "News not found.", response = ErrorResponse.class),
    })
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PostAuthorize("hasAnyAuthority('SA', 'TOZ') or " +
            "(hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER', 'ANONYMOUS') and returnObject.type == 'RELEASED')")
    public NewsView getNewsById(@PathVariable UUID id) {
        NewsView newsView = ModelMapper.convertToView(newsService.findById(id), NewsView.class);
        return newsView;
    }

    @ApiOperation(value = "Create news.", response = NewsView.class, notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request.", response = ValidationErrorResponse.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public ResponseEntity<NewsView> createNews(@Valid @RequestBody NewsView newsView) {
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(baseLocation)
                .body(ModelMapper.convertToView(newsService.createNews(convertFromView(newsView)), NewsView.class));
    }

    @ApiOperation(value = "Delete news.", notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "News not found.", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public ResponseEntity<?> deleteNewsById(@PathVariable UUID id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Update news.", response = NewsView.class, notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "News not found.", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Bad request.", response = ValidationErrorResponse.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public ResponseEntity<NewsView> updateNews(@PathVariable UUID id, @Valid @RequestBody NewsView newsView) {
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(baseLocation)
                .body(ModelMapper.convertToView(newsService.updateNews(id, convertFromView(newsView)), NewsView.class));
    }

    private static News convertFromView(final NewsView newsView) {
        return ModelMapper.convertToModel(newsView, News.class);
    }
}
