package com.intive.patronage.toz.status;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.status.model.PetStatus;
import com.intive.patronage.toz.status.model.PetStatusView;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(DataProviderRunner.class)
@SpringBootTest
public class PetStatusControllerTest {
    private static final int STATUS_LIST_SIZE = 3;
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Reksio";
    private static final String EXPECTED_RGB = "#cedaa2";
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
        final PetStatus petStatus = new PetStatus();
        petStatus.setId(EXPECTED_ID);
        petStatus.setName(EXPECTED_NAME);
        petStatus.setRgb(EXPECTED_RGB);

        final PetStatusView petStatusView = ModelMapper.convertToView(petStatus, PetStatusView.class);
        return new Object[][]{{petStatus, petStatusView}};
    }

    private List<PetStatus> getPetsStatuses() {
        final List<PetStatus> petStatuses = new ArrayList<>();
        for (int i = 0; i < STATUS_LIST_SIZE; i++) {
            final PetStatus petStatus = new PetStatus();
            petStatus.setId(UUID.randomUUID());
            petStatus.setName(String.format("%s%d", "name", i));
            petStatus.setRgb(String.format("%s%d", "rgbrgb", i));
            petStatuses.add(petStatus);
        }
        return petStatuses;
    }

    @Test
    public void shouldReturnPetsStatusList() throws Exception {
        final List<PetStatus> petStatuses = getPetsStatuses();
        when(petsStatusService.findAll()).thenReturn(petStatuses);

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
    public void shouldCreatePetsStatus(final PetStatus petStatus, final PetStatusView petStatusView) throws Exception {
        final String petsStatusViewJsonString = ModelMapper.convertToJsonString(petStatusView);

        when(petsStatusService.create(any(PetStatus.class))).thenReturn(petStatus);
        mvc.perform(post(ApiUrl.PETS_STATUS_PATH)
                .contentType(CONTENT_TYPE)
                .content(petsStatusViewJsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)));

        verify(petsStatusService, times(1))
                .create(any(PetStatus.class));
        verifyNoMoreInteractions(petsStatusService);
    }

    @Test
    @UseDataProvider("getPetsStatusModelAndView")
    public void shouldUpdatePetsStatus(final PetStatus petStatus, final PetStatusView petStatusView) throws Exception {
        final String petsStatusJsonString = ModelMapper.convertToJsonString(petStatusView);

        when(petsStatusService.update(eq(EXPECTED_ID), any(PetStatus.class))).thenReturn(petStatus);
        mvc.perform(put(String.format("%s/%s", ApiUrl.PETS_STATUS_PATH, EXPECTED_ID))
                .contentType(CONTENT_TYPE)
                .content(petsStatusJsonString))
                .andExpect(status().isOk());

        verify(petsStatusService, times(1)).update(eq(EXPECTED_ID), any(PetStatus.class));
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
