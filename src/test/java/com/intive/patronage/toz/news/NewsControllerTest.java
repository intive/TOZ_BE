package com.intive.patronage.toz.news;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.news.model.db.News;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(DataProviderRunner.class)
@SpringBootTest(
        properties = ApiProperties.JWT_SECRET_BASE64
)
public class NewsControllerTest {
    private static final int NEWS_LIST_SIZE = 5;
    private static final Boolean DEFAULT_SHORTENED = false;
    private static final Boolean SHORTENED_FOR_TEST = true;
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_TITLE = "New dog in TOZ!";
    private static final String EXPECTED_CONTENTS = "Today to our facility in Szczecin arrived a new dog. " +
            "His name is Reksio, he is two years old dachshund. " +
            "He was found in the neighborhood of allotment gardens.";
    private static final String EXPECTED_SHORTENED_CONTENTS = "Today to our facility in Szczecin arrived a " +
            "new dog. His name is Reksio, he is two years old ";
    private static final String DEFAULT_TYPE = News.Type.RELEASED.toString();
    private static final String DEFAULT_TYPE_AS_STRING = "RELEASED";
    private static final Boolean DEFAULT_ORDERED = false;
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Mock
    private NewsService newsService;
    private MockMvc mvc;

    @DataProvider
    public static Object[] getProperNews() {
        News news = new News();
        news.setId(EXPECTED_ID);
        news.setTitle(EXPECTED_TITLE);
        news.setContents(EXPECTED_CONTENTS);
        news.setType(News.Type.valueOf(DEFAULT_TYPE));
        return new News[]{news};
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(new NewsController(newsService)).build();
    }

    @Test
    public void getAllNews() throws Exception {
        final List<News> newsList = getNewsList(DEFAULT_TYPE_AS_STRING, DEFAULT_SHORTENED);
        when(newsService.findAllNews(DEFAULT_TYPE, DEFAULT_SHORTENED, DEFAULT_ORDERED)).thenReturn(newsList);

        mvc.perform(get(ApiUrl.NEWS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$[0].contents", is(EXPECTED_CONTENTS)))
                .andExpect(jsonPath("$", hasSize(NEWS_LIST_SIZE)));

        verify(newsService, times(1)).
                findAllNews(DEFAULT_TYPE, DEFAULT_SHORTENED, DEFAULT_ORDERED);
        verifyNoMoreInteractions(newsService);
    }

    @Test
    public void getAllNewsByType() throws Exception {
        final List<News> newsList = getNewsList(DEFAULT_TYPE, DEFAULT_SHORTENED);
        when(newsService.findAllNews(DEFAULT_TYPE, DEFAULT_SHORTENED, DEFAULT_ORDERED)).thenReturn(newsList);

        mvc.perform(get(ApiUrl.NEWS_PATH).param("type", DEFAULT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$[0].type", is(DEFAULT_TYPE)))
                .andExpect(jsonPath("$[1].type", is(DEFAULT_TYPE)))
                .andExpect(jsonPath("$", hasSize(NEWS_LIST_SIZE)));

        verify(newsService, times(1)).
                findAllNews(DEFAULT_TYPE, DEFAULT_SHORTENED, DEFAULT_ORDERED);
        verifyNoMoreInteractions(newsService);
    }

    @Test
    public void getAllNewsShortened() throws Exception {
        final List<News> newsList = getNewsList(DEFAULT_TYPE_AS_STRING, SHORTENED_FOR_TEST);
        when(newsService.findAllNews(DEFAULT_TYPE, SHORTENED_FOR_TEST, DEFAULT_ORDERED)).thenReturn(newsList);

        mvc.perform(get(ApiUrl.NEWS_PATH).param("isShortened", SHORTENED_FOR_TEST.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$[0].contents", is(EXPECTED_SHORTENED_CONTENTS)))
                .andExpect(jsonPath("$[1].contents", is(EXPECTED_SHORTENED_CONTENTS)))
                .andExpect(jsonPath("$", hasSize(NEWS_LIST_SIZE)));

        verify(newsService, times(1)).
                findAllNews(DEFAULT_TYPE, SHORTENED_FOR_TEST, DEFAULT_ORDERED);
        verifyNoMoreInteractions(newsService);
    }

    private List<News> getNewsList(String type, Boolean shortened) {
        final List<News> newsList = new ArrayList<>();
        for (int i = 0; i < NEWS_LIST_SIZE; i++) {
            News news = new News();
            news.setTitle(String.format("Title:%d", i));
            if (!shortened) {
                news.setContents(EXPECTED_CONTENTS);
            } else {
                news.setContents(EXPECTED_SHORTENED_CONTENTS);
            }
            if (type.equals(DEFAULT_TYPE)) {
                news.setType(News.Type.RELEASED);
            } else {
                news.setType(News.Type.values()[i % 2]);
            }
            newsList.add(news);
        }
        return newsList;
    }

    @Test
    @UseDataProvider("getProperNews")
    public void getNewsById(final News news) throws Exception {
        when(newsService.findById(EXPECTED_ID)).thenReturn(news);
        mvc.perform(get(String.format("%s/%s", ApiUrl.NEWS_PATH, EXPECTED_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.id", is(EXPECTED_ID.toString())))
                .andExpect(jsonPath("$.title", is(EXPECTED_TITLE)))
                .andExpect(jsonPath("$.contents", is(EXPECTED_CONTENTS)))
                .andExpect(jsonPath("$.type", is(DEFAULT_TYPE)));

        verify(newsService, times(1)).findById(EXPECTED_ID);
        verifyNoMoreInteractions(newsService);
    }

    @Test
    @UseDataProvider("getProperNews")
    public void createNews(final News news) throws Exception {
        when(newsService.createNews(any(News.class))).thenReturn(news);
        mvc.perform(post(ApiUrl.NEWS_PATH)
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(news)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(EXPECTED_ID.toString())))
                .andExpect(jsonPath("$.title", is(EXPECTED_TITLE)))
                .andExpect(jsonPath("$.contents", is(EXPECTED_CONTENTS)))
                .andExpect(jsonPath("$.type", is(DEFAULT_TYPE)));

        verify(newsService, times(1)).
                createNews(any(News.class));
        verifyNoMoreInteractions(newsService);
    }

    @Test
    @UseDataProvider("getProperNews")
    public void updateNews(final News news) throws Exception {
        when(newsService.updateNews(eq(EXPECTED_ID), any(News.class))).
                thenReturn(news);
        mvc.perform(put(String.format("%s/%s", ApiUrl.NEWS_PATH, EXPECTED_ID))
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(news)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(EXPECTED_ID.toString())))
                .andExpect(jsonPath("$.title", is(EXPECTED_TITLE)))
                .andExpect(jsonPath("$.contents", is(EXPECTED_CONTENTS)))
                .andExpect(jsonPath("$.type", is(DEFAULT_TYPE)));

        verify(newsService, times(1)).
                updateNews(eq(EXPECTED_ID), any(News.class));
        verifyNoMoreInteractions(newsService);
    }

    @Test
    public void deleteNewsById() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(newsService).deleteNews(id);
        mvc.perform(delete(String.format("%s/%s", ApiUrl.NEWS_PATH, id)))
                .andExpect(status().isOk());

        verify(newsService, times(1)).deleteNews(id);
        verifyNoMoreInteractions(newsService);
    }
}
