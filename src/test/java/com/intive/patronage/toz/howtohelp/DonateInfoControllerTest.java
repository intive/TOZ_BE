package com.intive.patronage.toz.howtohelp;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.howtohelp.model.db.HelpInfo;
import com.intive.patronage.toz.howtohelp.model.enumeration.HelpInfoType;
import com.intive.patronage.toz.util.ModelMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.intive.patronage.toz.howtohelp.DonateInfoServiceTest.DESCRIPTION;
import static com.intive.patronage.toz.howtohelp.DonateInfoServiceTest.MODIFICATION_DATE;
import static com.intive.patronage.toz.howtohelp.model.enumeration.HelpInfoType.HOW_TO_DONATE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DonateInfoControllerTest {

    private static final HelpInfoType INFO_TYPE = HOW_TO_DONATE;
    static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
    static final String DESCRIPTION_FIELD = "howToHelpDescription";

    @Mock
    private DonateInfoService donateInfoService;
    private MockMvc mvc;
    private static final HelpInfo helpInfo = new HelpInfo(INFO_TYPE, DESCRIPTION, MODIFICATION_DATE);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(new DonateInfoController(donateInfoService)).build();
    }

    @Test
    public void shouldGetHowToDonateInfo() throws Exception {
        when(donateInfoService.findHelpInfo(anyBoolean())).thenReturn(helpInfo);

        mvc.perform(get(ApiUrl.HOW_TO_DONATE_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath(DESCRIPTION_FIELD).value(DESCRIPTION));
    }

    @Test
    public void shouldCreateHowToDonateInfo() throws Exception {
        when(donateInfoService.createHelpInfo(any(HelpInfo.class))).thenReturn(helpInfo);

        mvc.perform(post(ApiUrl.HOW_TO_DONATE_PATH)
                .contentType(CONTENT_TYPE)
                .content(ModelMapper.convertToJsonString(helpInfo)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath(DESCRIPTION_FIELD).value(DESCRIPTION));
    }

    @Test
    public void shouldUpdateHowToDonate() throws Exception {
        when(donateInfoService.updateHelpInfo(any(HelpInfo.class))).thenReturn(helpInfo);

        mvc.perform(put(ApiUrl.HOW_TO_DONATE_PATH)
                .contentType(CONTENT_TYPE)
                .content(ModelMapper.convertToJsonString(helpInfo)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath(DESCRIPTION_FIELD).value(DESCRIPTION));
    }

    @Test
    public void shouldReturnErrorWhenMediaTypeHeaderIsMissingInPutRequest() throws Exception {
        mvc.perform(put(ApiUrl.HOW_TO_DONATE_PATH))
                .andExpect(status().isUnsupportedMediaType());

        verifyZeroInteractions(donateInfoService);
    }

    @Test
    public void shouldReturnErrorWhenBodyIsMissingInPutRequest() throws Exception {
        mvc.perform(put(ApiUrl.HOW_TO_DONATE_PATH)
                .contentType(CONTENT_TYPE))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(donateInfoService);
    }

    @Test
    public void shouldReturnErrorWhenMediaTypeHeaderIsMissingInPostRequest() throws Exception {
        mvc.perform(post(ApiUrl.HOW_TO_DONATE_PATH))
                .andExpect(status().isUnsupportedMediaType());

        verifyZeroInteractions(donateInfoService);
    }

    @Test
    public void shouldReturnErrorWhenBodyIsMissingInPostRequest() throws Exception {
        mvc.perform(post(ApiUrl.HOW_TO_DONATE_PATH)
                .contentType(CONTENT_TYPE))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(donateInfoService);
    }
}
