package com.intive.patronage.toz.status;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.status.model.PetsStatus;
import com.intive.patronage.toz.status.model.PetsStatusView;
import com.intive.patronage.toz.util.ModelMapper;
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
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(DataProviderRunner.class)
@SpringBootTest
public class PetsStatusControllerTest {
    private static final int STATUS_LIST_SIZE = 3;
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Reksio";
    private static final String EXPECTED_RGB = "#7cedaa2";
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Mock
    private PetsStatusService petsStatusService;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        final PetsStatusController petsStatusController = new PetsStatusController(petsStatusService);
        mvc = MockMvcBuilders.standaloneSetup(petsStatusController).build();
    }

    @DataProvider
    public static Object[][] getPetsStatusModelAndView() {
        final PetsStatus petsStatus = new PetsStatus();
        petsStatus.setId(EXPECTED_ID);
        petsStatus.setName(EXPECTED_NAME);
        petsStatus.setRgb(EXPECTED_RGB);

        final PetsStatusView petsStatusView = ModelMapper.convertToView(petsStatus, PetsStatusView.class);
        return new Object[][]{{petsStatus, petsStatusView}};
    }

    private List<PetsStatus> getPetsStatuses() {
        final List<PetsStatus> petsStatuses = new ArrayList<>();
        for (int i = 0; i < STATUS_LIST_SIZE; i++) {
            final PetsStatus petsStatus = new PetsStatus();
            petsStatus.setId(UUID.randomUUID());
            petsStatus.setName(String.format("%s%d", "name", i));
            petsStatus.setRgb(String.format("%s%d", "rgbrgb", i));
            petsStatuses.add(petsStatus);
        }
        return petsStatuses;
    }

    @Test
    public void shouldReturnPetsStatusList() throws Exception {
        final List<PetsStatus> petsStatuses = getPetsStatuses();
        when(petsStatusService.findAll()).thenReturn(petsStatuses);

        mvc.perform(get(ApiUrl.PETS_STATUS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(STATUS_LIST_SIZE)))
                .andExpect(jsonPath("$[0].name", notNullValue()))
                .andExpect(jsonPath("$[1].name", notNullValue()))
                .andExpect(jsonPath("$[2].name", notNullValue()));
        verify(petsStatusService, times(1)).findAll();
        verifyNoMoreInteractions(petsStatusService);
    }

    @Test
    @UseDataProvider("getPetsStatusModelAndView")
    public void shouldCreatePetsStatus(final PetsStatus petsStatus, final PetsStatusView petsStatusView) throws Exception {
        final String petsStatusViewJsonString = ModelMapper.convertToJsonString(petsStatusView);

        when(petsStatusService.create(any(PetsStatus.class))).thenReturn(petsStatus);
        mvc.perform(post(ApiUrl.PETS_STATUS_PATH)
                .contentType(CONTENT_TYPE)
                .content(petsStatusViewJsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)));

        verify(petsStatusService, times(1))
                .create(any(PetsStatus.class));
        verifyNoMoreInteractions(petsStatusService);
    }

    @Test
    @UseDataProvider("getPetsStatusModelAndView")
    public void shouldUpdatePetsStatus(final PetsStatus petsStatus, final PetsStatusView petsStatusView) throws Exception {
        final String petsStatusJsonString = ModelMapper.convertToJsonString(petsStatusView);

        when(petsStatusService.update(eq(EXPECTED_ID), any(PetsStatus.class))).thenReturn(petsStatus);
        mvc.perform(put(String.format("%s/%s", ApiUrl.PETS_STATUS_PATH, EXPECTED_ID))
                .contentType(CONTENT_TYPE)
                .content(petsStatusJsonString))
                .andExpect(status().isOk());

        verify(petsStatusService, times(1)).update(eq(EXPECTED_ID), any(PetsStatus.class));
        verifyNoMoreInteractions(petsStatusService);
    }

    @Test
    public void shouldDeletePetsStatusById() throws Exception {
        final UUID id = UUID.randomUUID();
        doNothing().when(petsStatusService).delete(id);
        mvc.perform(delete(String.format("%s/%s", ApiUrl.PETS_STATUS_PATH, id)))
                .andExpect(status().isOk());

        verify(petsStatusService, times(1)).delete(id);
        verifyNoMoreInteractions(petsStatusService);
    }
}
