package com.intive.patronage.toz.service;

import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.exception.WrongEnumValueException;
import com.intive.patronage.toz.model.db.News;
import com.intive.patronage.toz.model.view.NewsView;
import com.intive.patronage.toz.repository.NewsRepository;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(DataProviderRunner.class)
public class NewsServiceTest {
    private static final String EXPECTED_TITLE = "New dog in TOZ!";
    private static final String EXPECTED_CONTENTS = "Today to our facility in Szczecin arrived a " +
            "new dog. His name is Reksio, he is two years old dachshund. He was found in the " +
            "neighborhood of allotment gardens.";
    private static final News.Type EXPECTED_TYPE = News.Type.RELEASED;
    private static final String WRONG_TYPE = "published";
    private static final News.Type EXPECTED_TYPE_VALUE = EXPECTED_TYPE;
    private static final Boolean DEFAULT_SHORTENED = false;
    private static final String DEFAULT_TYPE = null;
    private static final Long EXPECTED_PUBLISHED = 12412412412L;
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final Date EXPECTED_PUBLISHED_DATE = new Date(12412412412L);
    private NewsView newsView;

    @Mock
    private NewsRepository newsRepository;

    private NewsService newsService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        newsService = new NewsService(newsRepository);
        newsView = new NewsView();
        newsView.setTitle(EXPECTED_TITLE);
        newsView.setContents(EXPECTED_CONTENTS);
        newsView.setType(EXPECTED_TYPE_VALUE.toString());
        newsView.setPublished(EXPECTED_PUBLISHED);
    }

    @DataProvider
    public static Object[] getProperNews() {
        News newsDb = new News();
        newsDb.setId(EXPECTED_ID);
        newsDb.setTitle(EXPECTED_TITLE);
        newsDb.setContents(EXPECTED_CONTENTS);
        newsDb.setType(EXPECTED_TYPE);
        newsDb.setPublished(new Date(EXPECTED_PUBLISHED));
        return new News[]{newsDb};
    }

    @Test
    public void findAllNews() throws Exception {
        when(newsRepository.findAll()).thenReturn(Collections.emptyList());

        List<News> newsList = newsService.findAllNews(DEFAULT_TYPE, DEFAULT_SHORTENED);
        assertTrue(newsList.isEmpty());
    }

    @Test
    public void findAllNewsByType() throws Exception {
        when(newsRepository.findByType(EXPECTED_TYPE)).thenReturn(Collections.emptyList());

        List<News> newsList = newsService.
                findAllNews(EXPECTED_TYPE.toString(), DEFAULT_SHORTENED);
        assertTrue(newsList.isEmpty());
    }

    @Test(expected = WrongEnumValueException.class)
    public void findAllNewsByTypeWrongEnumValueException() throws Exception {
        when(newsRepository.findByType(EXPECTED_TYPE)).thenReturn(Collections.emptyList());

        List<News> newsList = newsService.findAllNews(WRONG_TYPE, DEFAULT_SHORTENED);
        assertTrue(newsList.isEmpty());
    }

    @Test
    @UseDataProvider("getProperNews")
    public void findById(final News newsDb) throws Exception {
        when(newsRepository.exists(EXPECTED_ID)).thenReturn(true);
        when(newsRepository.findOne(EXPECTED_ID)).thenReturn(newsDb);

        News dbNews = newsService.findById(EXPECTED_ID);
        assertEquals(EXPECTED_TITLE, dbNews.getTitle());
        assertEquals(EXPECTED_CONTENTS, dbNews.getContents());
        assertEquals(EXPECTED_TYPE_VALUE, dbNews.getType());

        verify(newsRepository, times(1)).exists(eq(EXPECTED_ID));
        verify(newsRepository, times(1)).findOne(eq(EXPECTED_ID));
        verifyNoMoreInteractions(newsRepository);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdNotFoundException() throws Exception {
        when(newsRepository.exists(EXPECTED_ID)).thenReturn(false);
        newsService.findById(EXPECTED_ID);

        verify(newsRepository, times(1)).exists(eq(EXPECTED_ID));
        verifyNoMoreInteractions(newsRepository);
    }

    @Test
    @UseDataProvider("getProperNews")
    public void createNews(final News newsDb) throws Exception {
        when(newsRepository.save(any(News.class))).thenReturn(newsDb);

        News news = newsService.createNews(newsView);
        assertEquals(EXPECTED_TITLE, news.getTitle());
        assertEquals(EXPECTED_CONTENTS, news.getContents());
        assertEquals(EXPECTED_TYPE_VALUE, news.getType());

        verify(newsRepository, times(1)).save(any(News.class));
        verifyNoMoreInteractions(newsRepository);
    }

    @Test
    public void deleteNews() throws Exception {
        when(newsRepository.exists(EXPECTED_ID)).thenReturn(true);
        newsService.deleteNews(EXPECTED_ID);

        verify(newsRepository, times(1)).exists(eq(EXPECTED_ID));
        verify(newsRepository, times(1)).delete(eq(EXPECTED_ID));
        verifyNoMoreInteractions(newsRepository);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNewsNotFoundException() throws Exception {
        when(newsRepository.exists(EXPECTED_ID)).thenReturn(false);
        newsService.deleteNews(EXPECTED_ID);

        verify(newsRepository, times(1)).exists(eq(EXPECTED_ID));
        verifyNoMoreInteractions(newsRepository);
    }

    @Test
    @UseDataProvider("getProperNews")
    public void updateNews(final News newsDb) throws Exception {
        when(newsRepository.exists(EXPECTED_ID)).thenReturn(true);
        when(newsRepository.save(any(News.class))).thenReturn(newsDb);
        when(newsRepository.findOne(EXPECTED_ID)).thenReturn(newsDb);
        News news = newsService.updateNews(EXPECTED_ID, newsView);

        assertEquals(EXPECTED_TITLE, news.getTitle());
        assertEquals(EXPECTED_TYPE_VALUE, news.getType());
        assertEquals(EXPECTED_CONTENTS, news.getContents());
        assertEquals(EXPECTED_PUBLISHED_DATE, news.getPublished());
    }

    @Test(expected = NotFoundException.class)
    public void updateNewsNotFoundException() throws Exception {
        when(newsRepository.exists(EXPECTED_ID)).thenReturn(false);
        newsService.updateNews(EXPECTED_ID, newsView);

        verify(newsRepository, times(1)).exists(eq(EXPECTED_ID));
        verifyNoMoreInteractions(newsRepository);
    }
}
