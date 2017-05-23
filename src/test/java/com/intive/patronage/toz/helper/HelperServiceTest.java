package com.intive.patronage.toz.helper;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.helper.model.db.Helper;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(DataProviderRunner.class)
public class HelperServiceTest {
    private static final Helper.Category EXPECTED_CATEGORY = Helper.Category.GUARDIAN;
    private static final Helper.Category DEFAULT_CATEGORY = null;
    private static final String EXPECTED_NAME = "Jan";
    private static final String EXPECTED_SURNAME = "Kowalski";
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private Helper helper;

    @Mock
    private HelperRepository helperRepository;

    private HelperService helperService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        helperService = new HelperService(helperRepository);
        helper = new Helper();
        helper.setName(EXPECTED_NAME);
        helper.setSurname(EXPECTED_SURNAME);
        helper.setCategory(EXPECTED_CATEGORY);
    }

    @DataProvider
    public static Object[] getProperHelper() {
        Helper helperDb = new Helper();
        helperDb.setId(EXPECTED_ID);
        helperDb.setName(EXPECTED_NAME);
        helperDb.setSurname(EXPECTED_SURNAME);
        helperDb.setCategory(EXPECTED_CATEGORY);
        return new Helper[]{helperDb};
    }

    @Test
    public void findAllHelpers() throws Exception {
        when(helperRepository.findAll()).thenReturn(Collections.emptyList());

        List<Helper> helperList = helperService.findAllHelpers(DEFAULT_CATEGORY);
        assertTrue(helperList.isEmpty());
    }

    @Test
    public void findAllHelpersByCategory() throws Exception {
        when(helperRepository.findByCategory(EXPECTED_CATEGORY))
                .thenReturn(Collections.emptyList());

        List<Helper> helperList = helperService.
                findAllHelpers(EXPECTED_CATEGORY);
        assertTrue(helperList.isEmpty());
    }

    @Test
    @UseDataProvider("getProperHelper")
    public void findById(final Helper helperDb) throws Exception {
        when(helperRepository.exists(EXPECTED_ID)).thenReturn(true);
        when(helperRepository.findOne(EXPECTED_ID)).thenReturn(helperDb);

        Helper helper = helperService.findById(EXPECTED_ID);
        assertEquals(EXPECTED_NAME, helper.getName());
        assertEquals(EXPECTED_SURNAME, helper.getSurname());
        assertEquals(EXPECTED_CATEGORY, helper.getCategory());

        verify(helperRepository, times(1)).exists(eq(EXPECTED_ID));
        verify(helperRepository, times(1)).findOne(eq(EXPECTED_ID));
        verifyNoMoreInteractions(helperRepository);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdNotFoundException() throws Exception {
        when(helperRepository.exists(EXPECTED_ID)).thenReturn(false);
        helperService.findById(EXPECTED_ID);

        verify(helperRepository, times(1)).exists(eq(EXPECTED_ID));
        verifyNoMoreInteractions(helperRepository);
    }

    @Test
    @UseDataProvider("getProperHelper")
    public void createHelper(final Helper helperDb) throws Exception {
        when(helperRepository.save(any(Helper.class))).thenReturn(helperDb);

        Helper helper = helperService.createHelper(this.helper);
        assertEquals(EXPECTED_NAME, helper.getName());
        assertEquals(EXPECTED_SURNAME, helper.getSurname());
        assertEquals(EXPECTED_CATEGORY, helper.getCategory());

        verify(helperRepository, times(1)).save(any(Helper.class));
        verifyNoMoreInteractions(helperRepository);
    }

    @Test
    public void deleteHelper() throws Exception {
        when(helperRepository.exists(EXPECTED_ID)).thenReturn(true);
        helperService.deleteHelper(EXPECTED_ID);

        verify(helperRepository, times(1)).exists(eq(EXPECTED_ID));
        verify(helperRepository, times(1)).delete(eq(EXPECTED_ID));
        verifyNoMoreInteractions(helperRepository);
    }

    @Test(expected = NotFoundException.class)
    public void deleteHelperNotFoundException() throws Exception {
        when(helperRepository.exists(EXPECTED_ID)).thenReturn(false);
        helperService.deleteHelper(EXPECTED_ID);

        verify(helperRepository, times(1)).exists(eq(EXPECTED_ID));
        verifyNoMoreInteractions(helperRepository);
    }

    @Test
    @UseDataProvider("getProperHelper")
    public void updateHelper(final Helper helperDb) throws Exception {
        when(helperRepository.exists(EXPECTED_ID)).thenReturn(true);
        when(helperRepository.save(any(Helper.class))).thenReturn(helperDb);
        when(helperRepository.findOne(EXPECTED_ID)).thenReturn(helperDb);
        Helper helper = helperService.updateHelper(EXPECTED_ID, this.helper);

        assertEquals(EXPECTED_NAME, helper.getName());
        assertEquals(EXPECTED_SURNAME, helper.getSurname());
        assertEquals(EXPECTED_CATEGORY, helper.getCategory());
    }

    @Test(expected = NotFoundException.class)
    public void updateHelperNotFoundException() throws Exception {
        when(helperRepository.exists(EXPECTED_ID)).thenReturn(false);
        helperService.updateHelper(EXPECTED_ID, helper);

        verify(helperRepository, times(1)).exists(eq(EXPECTED_ID));
        verifyNoMoreInteractions(helperRepository);
    }
}
