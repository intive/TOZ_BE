package com.intive.patronage.toz.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.helper.model.db.Helper;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(DataProviderRunner.class)
@SpringBootTest(
        properties = ApiProperties.JWT_SECRET_BASE64
)
public class HelperControllerTest {
    private static final int HELPERS_LIST_SIZE = 5;
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final Helper.Category EXPECTED_CATEGORY = Helper.Category.GUARDIAN;
    private static final String EXPECTED_NAME = "Jan";
    private static final String EXPECTED_SURNAME = "Kowalski";
    private static final Helper.Category DEFAULT_CATEGORY = null;
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Mock
    private HelperService helperService;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new HelperController(helperService))
                .build();
    }

    @DataProvider
    public static Object[] getProperHelper() {
        Helper helper = new Helper();
        helper.setId(EXPECTED_ID);
        helper.setName(EXPECTED_NAME);
        helper.setSurname(EXPECTED_SURNAME);
        helper.setCategory(EXPECTED_CATEGORY);
        return new Helper[]{helper};
    }

    @Test
    public void getAllHelpers() throws Exception {
        final List<Helper> helperList = getHelperList(DEFAULT_CATEGORY);
        when(helperService.findAllHelpers(DEFAULT_CATEGORY)).thenReturn(helperList);

        mockMvc.perform(get(ApiUrl.HELPERS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(HELPERS_LIST_SIZE)));

        verify(helperService, times(1))
                .findAllHelpers(DEFAULT_CATEGORY);
        verifyNoMoreInteractions(helperService);
    }

    @Test
    public void getAllHelpersByCategory() throws Exception {
        final List<Helper> helperList = getHelperList(EXPECTED_CATEGORY);
        when(helperService.findAllHelpers(EXPECTED_CATEGORY))
                .thenReturn(helperList);

        mockMvc.perform(get(ApiUrl.HELPERS_PATH).param("category", EXPECTED_CATEGORY
                .toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$[0].category", is(EXPECTED_CATEGORY
                        .toString())))
                .andExpect(jsonPath("$[1].category", is(EXPECTED_CATEGORY
                        .toString())))
                .andExpect(jsonPath("$", hasSize(HELPERS_LIST_SIZE)));

        verify(helperService, times(1)).
                findAllHelpers(EXPECTED_CATEGORY);
        verifyNoMoreInteractions(helperService);
    }

    private List<Helper> getHelperList(Helper.Category category) {
        final List<Helper> helperList = new ArrayList<>();
        for (int i = 0; i < HELPERS_LIST_SIZE; i++) {
            Helper helper = new Helper();
            helper.setName(String.format("Name:%d", i));
            helper.setSurname(String.format("Surname:%d", i));
            if (category == null) {
                helper.setCategory(Helper.Category.values()[i % 2]);
            } else {
                helper.setCategory(EXPECTED_CATEGORY);
            }
            helperList.add(helper);
        }
        return helperList;
    }

    @Test
    @UseDataProvider("getProperHelper")
    public void getHelperById(final Helper helper) throws Exception {
        when(helperService.findById(EXPECTED_ID)).thenReturn(helper);
        mockMvc.perform(get(String.format("%s/%s", ApiUrl.HELPERS_PATH, EXPECTED_ID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.id", is(EXPECTED_ID.toString())))
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.surname", is(EXPECTED_SURNAME)));

        verify(helperService, times(1)).findById(EXPECTED_ID);
        verifyNoMoreInteractions(helperService);
    }

    @Test
    @UseDataProvider("getProperHelper")
    public void createHelper(final Helper helper) throws Exception {
        when(helperService.createHelper(any(Helper.class))).thenReturn(helper);
        mockMvc.perform(post(ApiUrl.HELPERS_PATH)
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(helper)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(EXPECTED_ID.toString())))
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.surname", is(EXPECTED_SURNAME)));

        verify(helperService, times(1)).
                createHelper(any(Helper.class));
        verifyNoMoreInteractions(helperService);
    }

    @Test
    @UseDataProvider("getProperHelper")
    public void updateHelper(final Helper helper) throws Exception {
        when(helperService.updateHelper(eq(EXPECTED_ID), any(Helper.class)))
                .thenReturn(helper);
        mockMvc.perform(put(String.format("%s/%s", ApiUrl.HELPERS_PATH, EXPECTED_ID))
                .contentType(CONTENT_TYPE)
                .content(new ObjectMapper().writeValueAsString(helper)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(EXPECTED_ID.toString())))
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.surname", is(EXPECTED_SURNAME)));

        verify(helperService, times(1))
                .updateHelper(eq(EXPECTED_ID), any(Helper.class));
        verifyNoMoreInteractions(helperService);
    }

    @Test
    public void deleteHelperById() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(helperService).deleteHelper(id);
        mockMvc.perform(delete(String.format("%s/%s", ApiUrl.HELPERS_PATH, id)))
                .andExpect(status().isOk());

        verify(helperService, times(1)).deleteHelper(id);
        verifyNoMoreInteractions(helperService);
    }
}
