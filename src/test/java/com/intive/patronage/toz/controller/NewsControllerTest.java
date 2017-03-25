package com.intive.patronage.toz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.model.db.News;
import com.intive.patronage.toz.model.view.NewsView;
import com.intive.patronage.toz.service.NewsService;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(DataProviderRunner.class)
public class NewsControllerTest {
    private static final int NEWS_LIST_SIZE = 5;
    private static final String PATH = "/news";
    private static final Boolean DEFAULT_SHORTENED = false;
    private static final Boolean SHORTENED_FOR_TEST = true;
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_TITLE = "New dog in TOZ!";
    private static final String EXPECTED_CONTENTS = "Today to our facility in Szczecin arrived a new dog. His name is Reksio, he is two years old dachshund. He was found in the neighborhood of allotment gardens.";
    private static final String EXPECTED_SHORTENED_CONTENTS = "Today to our facility in Szczecin arrived a new dog. His name is Reksio, he is two years old ";
    private static final String EXPECTED_TYPE = News.Type.RELEASED.toString();
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Mock
    private NewsService newsService;
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(new NewsController(newsService)).build();
    }

    @DataProvider
    public static Object[] getProperNews() {
        NewsView newsView = new NewsView();
        newsView.setId(EXPECTED_ID);
        newsView.setTitle(EXPECTED_TITLE);
        newsView.setContents(EXPECTED_CONTENTS);
        newsView.setType(EXPECTED_TYPE);
        return new NewsView[]{newsView};
    }

    @Test
    public void getAllNews() throws Exception {
        final List<NewsView> newsViews = getNewsList("", false);
        when(newsService.findAllNews(DEFAULT_SHORTENED)).thenReturn(newsViews);

        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(NEWS_LIST_SIZE)));

        verify(newsService, times(1)).findAllNews(DEFAULT_SHORTENED);
        verifyNoMoreInteractions(newsService);
    }

    @Test
    public void getAllNewsByType() throws Exception {
        final List<NewsView> newsViews = getNewsList(EXPECTED_TYPE, false);
        when(newsService.findAllNewsByType(EXPECTED_TYPE, DEFAULT_SHORTENED)).thenReturn(newsViews);

        mvc.perform(get(PATH + "?type=" + EXPECTED_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$[0].type", is(EXPECTED_TYPE)))
                .andExpect(jsonPath("$[1].type", is(EXPECTED_TYPE)))
                .andExpect(jsonPath("$", hasSize(NEWS_LIST_SIZE)));

        verify(newsService, times(1)).findAllNewsByType(EXPECTED_TYPE, DEFAULT_SHORTENED);
        verifyNoMoreInteractions(newsService);
    }

    @Test
    public void getAllNewsShortened() throws Exception {
        final List<NewsView> newsViews = getNewsList("", true);
        when(newsService.findAllNews(SHORTENED_FOR_TEST)).thenReturn(newsViews);

        mvc.perform(get(PATH + "?shortened=true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$[0].contents", is(EXPECTED_SHORTENED_CONTENTS)))
                .andExpect(jsonPath("$[1].contents", is(EXPECTED_SHORTENED_CONTENTS)))
                .andExpect(jsonPath("$", hasSize(NEWS_LIST_SIZE)));

        verify(newsService, times(1)).findAllNews(SHORTENED_FOR_TEST);
        verifyNoMoreInteractions(newsService);
    }

    private List<NewsView> getNewsList(String type, Boolean shortened) {
        final List<NewsView> newsViews = new ArrayList<>();
        for (int i = 0; i < NEWS_LIST_SIZE; i++) {
            NewsView newsView = new NewsView();
            newsView.setTitle("Title:" + i);
            if (shortened = false) {
                newsView.setContents(EXPECTED_CONTENTS);
            } else {
                newsView.setContents(EXPECTED_SHORTENED_CONTENTS);
            }
            if (type.equals(EXPECTED_TYPE)) {
                newsView.setType(News.Type.values()[0].toString());
            } else {
                newsView.setType(News.Type.values()[i % 2].toString());
            }
            newsViews.add(newsView);
        }
        return newsViews;
    }

    @Test
    @UseDataProvider("getProperNews")
    public void getNewsById(final NewsView newsView) throws Exception {
        when(newsService.findById(EXPECTED_ID)).thenReturn(newsView);
        mvc.perform(get(PATH + "/" + EXPECTED_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.id", is(EXPECTED_ID.toString())))
                .andExpect(jsonPath("$.title", is(EXPECTED_TITLE)))
                .andExpect(jsonPath("$.contents", is(EXPECTED_CONTENTS)))
                .andExpect(jsonPath("$.type", is(EXPECTED_TYPE)));

        verify(newsService, times(1)).findById(EXPECTED_ID);
        verifyNoMoreInteractions(newsService);
    }

    @Test
    @UseDataProvider("getProperNews")
    public void createNews(final NewsView newsView) throws Exception {
        when(newsService.createNews(any(NewsView.class))).thenReturn(newsView);
        mvc.perform(post(PATH)
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(newsView)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(EXPECTED_ID.toString())))
                .andExpect(jsonPath("$.title", is(EXPECTED_TITLE)))
                .andExpect(jsonPath("$.contents", is(EXPECTED_CONTENTS)))
                .andExpect(jsonPath("$.type", is(EXPECTED_TYPE)));

        verify(newsService, times(1)).createNews(any(NewsView.class));
        verifyNoMoreInteractions(newsService);
    }

    @Test
    @UseDataProvider("getProperNews")
    public void updateNews(final NewsView newsView) throws Exception {
        when(newsService.updateNews(eq(EXPECTED_ID), any(NewsView.class))).thenReturn(newsView);
        mvc.perform(put(PATH + "/" + EXPECTED_ID)
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(newsView)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(EXPECTED_ID.toString())))
                .andExpect(jsonPath("$.title", is(EXPECTED_TITLE)))
                .andExpect(jsonPath("$.contents", is(EXPECTED_CONTENTS)))
                .andExpect(jsonPath("$.type", is(EXPECTED_TYPE)));

        verify(newsService, times(1)).updateNews(eq(EXPECTED_ID), any(NewsView.class));
        verifyNoMoreInteractions(newsService);
    }

    @Test
    public void deleteNewsById() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(newsService).deleteNews(id);
        mvc.perform(delete(PATH + "/" + id))
                .andExpect(status().isOk());

        verify(newsService, times(1)).deleteNews(id);
        verifyNoMoreInteractions(newsService);
    }
}
