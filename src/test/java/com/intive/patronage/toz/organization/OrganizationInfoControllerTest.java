package com.intive.patronage.toz.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.ControllerExceptionHandler;
import com.intive.patronage.toz.organization.model.view.BankAccountView;
import com.intive.patronage.toz.organization.model.view.OrganizationInfoView;
import com.intive.patronage.toz.schedule.util.ScheduleParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.nio.charset.Charset;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrganizationInfoControllerTest {

    private final static String ORG_NAME = "Org";
    private final static String ACCOUNT = "63102047950000940201035419";
    private final static String INVALID_ACCOUNT = "631-02047950000940201035419";

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Mock
    private OrganizationInfoService organizationInfoService;
    @Mock
    private ScheduleParser scheduleParser;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MessageSource messageSource;

    private MockMvc mvc;
    private MockMvc mvcWithCustomHandlers;
    private OrganizationInfoView infoView;
    private OrganizationInfoView invalidInfoView;

    private ExceptionHandlerExceptionResolver createExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            @Override
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(
                    HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(
                        ControllerExceptionHandler.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(
                        new ControllerExceptionHandler(messageSource, scheduleParser), method);
            }
        };
        exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        exceptionResolver.afterPropertiesSet();
        return exceptionResolver;
    }

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new OrganizationInfoController(organizationInfoService)).build();
        mvcWithCustomHandlers = MockMvcBuilders.standaloneSetup(new OrganizationInfoController(organizationInfoService))
                .setHandlerExceptionResolvers(createExceptionResolver()).build();

        BankAccountView bankAccountView = new BankAccountView.Builder(ACCOUNT)
                .build();
        infoView = new OrganizationInfoView.Builder(ORG_NAME, bankAccountView)
                .build();
        BankAccountView invalidBankAccountView = new BankAccountView.Builder(INVALID_ACCOUNT)
                .build();
        invalidInfoView = new OrganizationInfoView.Builder(ORG_NAME, invalidBankAccountView)
                .build();
    }

    @Test
    public void shouldGetOrganizationInfo() throws Exception {
        when(organizationInfoService.findOrganizationInfo()).thenReturn(infoView);

        mvc.perform(get(ApiUrl.ORGANIZATION_INFO_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("name").value(ORG_NAME))
                .andExpect(jsonPath("bankAccount.number").value(ACCOUNT));
    }

    @Test
    public void shouldReturnErrorWhenMediaTypeHeaderIsMissingInPostRequest() throws Exception {
        mvc.perform(post(ApiUrl.ORGANIZATION_INFO_PATH))
                .andExpect(status().isUnsupportedMediaType());

        verifyZeroInteractions(organizationInfoService);
    }

    @Test
    public void shouldReturnErrorWhenBodyIsMissingInPostRequest() throws Exception {
        mvc.perform(post(ApiUrl.ORGANIZATION_INFO_PATH)
                .contentType(contentType))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(organizationInfoService);
    }

    @Test
    public void shouldCreateOrganizationInfo() throws Exception {
        when(organizationInfoService.createOrganizationInfo(any(OrganizationInfoView.class))).thenReturn(infoView);

        mvc.perform(post(ApiUrl.ORGANIZATION_INFO_PATH)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(infoView)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("name").value(ORG_NAME))
                .andExpect(jsonPath("bankAccount.number").value(ACCOUNT));
    }

    @Test
    public void shouldUpdateOrganizationInformation() throws Exception {
        when(organizationInfoService.updateOrganizationInfo(any(OrganizationInfoView.class))).thenReturn(infoView);

        mvc.perform(put(ApiUrl.ORGANIZATION_INFO_PATH)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(infoView)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("name").value(ORG_NAME))
                .andExpect(jsonPath("bankAccount.number").value(ACCOUNT));
    }

    @Test
    public void shouldReturnErrorWhenMediaTypeHeaderIsMissingInPutRequest() throws Exception {
        mvc.perform(put(ApiUrl.ORGANIZATION_INFO_PATH))
                .andExpect(status().isUnsupportedMediaType());

        verifyZeroInteractions(organizationInfoService);
    }

    @Test
    public void shouldReturnErrorWhenBodyIsMissingInPutRequest() throws Exception {
        mvc.perform(put(ApiUrl.ORGANIZATION_INFO_PATH)
                .contentType(contentType))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(organizationInfoService);
    }

    @Test()
    public void shouldReturnValidationErrorWhenInvalidFieldsInBodyInPutRequest() throws Exception {
        final String validationErrorMessage = messageSource.getMessage("validationError",
                null,
                LocaleContextHolder.getLocale());
        mvcWithCustomHandlers.perform(put(ApiUrl.ORGANIZATION_INFO_PATH)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(invalidInfoView)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("message").value(validationErrorMessage));
    }

    @Test()
    public void shouldReturnValidationErrorWhenInvalidFieldsInBodyInPostRequest() throws Exception {
        final String validationErrorMessage = messageSource.getMessage("validationError",
                null,
                LocaleContextHolder.getLocale());
        mvcWithCustomHandlers.perform(post(ApiUrl.ORGANIZATION_INFO_PATH)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(invalidInfoView)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("message").value(validationErrorMessage));
    }
}
