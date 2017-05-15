package com.intive.patronage.toz.news;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.news.model.db.News;
import com.intive.patronage.toz.news.model.view.NewsView;
import com.intive.patronage.toz.storage.StorageProperties;
import com.intive.patronage.toz.storage.StorageService;
import com.intive.patronage.toz.storage.model.db.UploadedFile;
import com.intive.patronage.toz.storage.model.view.UrlView;
import com.intive.patronage.toz.util.ModelMapper;
import com.intive.patronage.toz.util.UserInfoGetter;
import com.intive.patronage.toz.util.validation.ImageValidator;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Api(tags = "News", description = "News operations.")
@RestController
@RequestMapping(value = ApiUrl.NEWS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
class NewsController {
    private final static String DEFAULT_TYPE = "RELEASED";
    private final NewsService newsService;
    private final StorageService storageService;
    private final StorageProperties storageProperties;

    @Autowired
    NewsController(NewsService newsService, StorageService storageService,
                   StorageProperties storageProperties) {
        this.newsService = newsService;
        this.storageService = storageService;
        this.storageProperties = storageProperties;
    }

    @ApiOperation(value = "Get all news.", responseContainer = "List", notes =
            "Required roles: SA, TOZ, VOLUNTEER, ANONYMOUS for RELEASED type or " +
                    "SA, TOZ for other types.")
    @ApiResponses(value = {
            @ApiResponse(code = 422, message = "Unprocessable entity.", response = ErrorResponse.class)
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ') or " +
            "(hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER', 'ANONYMOUS') and (#type == 'RELEASED' or #type == null))")
    public ResponseEntity<List<NewsView>> getAllNews(
            @ApiParam(allowableValues = "ARCHIVED, RELEASED, UNRELEASED")
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "isShortened", required = false, defaultValue = "false")
                    Boolean isShortened,
            @RequestParam(value = "isOrdered", required = false, defaultValue = "false")
                    Boolean isOrdered) {
        if (UserInfoGetter.hasCurrentUserAdminRole()) {
            final List<News> newsList = newsService.findAllNews(type, isShortened, isOrdered);
            return ResponseEntity.ok().body(ModelMapper.convertToView(newsList, NewsView.class));
        }
        final List<News> newsList = newsService.findAllNews(DEFAULT_TYPE, isShortened, isOrdered);
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
        return ModelMapper.convertToView(newsService.findById(id), NewsView.class);
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

    @ApiOperation(value = "Upload image", notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 422, message = "Invalid image file")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public ResponseEntity<UrlView> uploadFile(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        ImageValidator.validateImageArgument(file);
        final UploadedFile uploadedFile = storageService.store(file);
        UrlView urlView = new UrlView();
        urlView.setUrl(String.format("%s/%s", this.storageProperties.getStoragePathRoot(), uploadedFile.getPath()));
        newsService.updateNewsImageUrl(id, urlView.getUrl());
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();

        return ResponseEntity.created(location)
                .body(urlView);
    }
}
