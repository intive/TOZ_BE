package com.intive.patronage.toz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.model.view.BankAccountView;
import com.intive.patronage.toz.model.view.OrganizationInfoView;
import com.intive.patronage.toz.service.OrganizationInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrganizationInfoControllerTest {

    private final static String PATH = "/organization/info";
    private final static String ORG_NAME = "Org";
    private final static String ACCOUNT = "63102047950000940201035419";

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Mock
    private OrganizationInfoService infoService;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;
    private OrganizationInfoView infoView;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new OrganizationInfoController(infoService)).build();

        BankAccountView bankAccountView = new BankAccountView.Builder(ACCOUNT)
                .build();
        infoView = new OrganizationInfoView.Builder(ORG_NAME, bankAccountView)
                .build();
    }

    @Test
    public void shouldGetOrganizationInfo() throws Exception {
        when(infoService.findOrganizationInfo()).thenReturn(infoView);

        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("name").value(ORG_NAME))
                .andExpect(jsonPath("bankAccount.number").value(ACCOUNT));
    }

    @Test
    public void shouldReturnErrorWhenMediaTypeHeaderIsMissingInPostRequest() throws Exception {
        mvc.perform(post(PATH))
                .andExpect(status().isUnsupportedMediaType());

        verifyZeroInteractions(infoService);
    }

    @Test
    public void shouldReturnErrorWhenBodyIsMissingInPostRequest() throws Exception {
        mvc.perform(post(PATH)
                .contentType(contentType))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(infoService);
    }

    @Test
    public void shouldCreateOrganizationInfo() throws Exception {
        when(infoService.createOrganizationInfo(any(OrganizationInfoView.class))).thenReturn(infoView);

        mvc.perform(post(PATH)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(infoView)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("name").value(ORG_NAME))
                .andExpect(jsonPath("bankAccount.number").value(ACCOUNT));
    }

    @Test
    public void shouldUpdateOrganizationInformation() throws Exception {
        when(infoService.updateOrganizationInfo(any(OrganizationInfoView.class))).thenReturn(infoView);

        mvc.perform(put(PATH)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(infoView)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("name").value(ORG_NAME))
                .andExpect(jsonPath("bankAccount.number").value(ACCOUNT));
    }

    @Test
    public void shouldReturnErrorWhenMediaTypeHeaderIsMissingInPutRequest() throws Exception {
        mvc.perform(put(PATH))
                .andExpect(status().isUnsupportedMediaType());

        verifyZeroInteractions(infoService);
    }

    @Test
    public void shouldReturnErrorWhenBodyIsMissingInPutRequest() throws Exception {
        mvc.perform(put(PATH)
                .contentType(contentType))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(infoService);
    }
}
