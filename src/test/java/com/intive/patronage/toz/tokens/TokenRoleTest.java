package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.Application;
import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.users.model.db.User;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;

import static com.intive.patronage.toz.tokens.TokenControllerTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(DataProviderRunner.class)
@ContextConfiguration(classes = Application.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {ApiProperties.JWT_SECRET_BASE64, ApiProperties.SUPER_ADMIN_PASSWORD}
)
@ActiveProfiles("test")
public class TokenRoleTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private static final String TYPE_PARAM = "type";
    private static final String RELEASED = "RELEASED";
    private static final String ACHIEVED = "ACHIEVED";
    private static final String EMAIL_VOLUNTEER = "volunteer@mail.com";
    private static final User.Role ROLE_VOLUNTEER = User.Role.VOLUNTEER;


    @Value("${jwt.expiration-time-minutes}")
    private long expirationTime;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtFactory jwtFactory;

    @DataProvider
    public static Object[] getAdmin() {
        User admin = new User();
        admin.addRole(ROLE_ADMIN);
        admin.setEmail(EMAIL);
        return new User[]{admin};
    }

    @DataProvider
    public static Object[] getVolunteer() {
        User volunteer = new User();
        volunteer.addRole(ROLE_VOLUNTEER);
        volunteer.setEmail(EMAIL_VOLUNTEER);
        return new User[]{volunteer};
    }

    @Test
    @UseDataProvider("getAdmin")
    public void shouldReturnOkWhenAdminAndGetNewsReleased(User admin) throws Exception {
        mockMvc.perform(get(ApiUrl.NEWS_PATH).param(TYPE_PARAM, RELEASED)
                .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + jwtFactory.generateTokenWithSpecifiedExpirationTime(admin, expirationTime)))
                .andExpect(status().isOk());
    }

    @Test
    @UseDataProvider("getVolunteer")
    public void shouldReturnForbiddenWhenVolunteerAndNewsAchieved(User volunteer) throws Exception {
        mockMvc.perform(get(ApiUrl.NEWS_PATH).param(TYPE_PARAM, ACHIEVED)
                .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + jwtFactory.generateTokenWithSpecifiedExpirationTime(volunteer, expirationTime)))
                .andExpect(status().isForbidden());
    }

    @Test
    @UseDataProvider("getVolunteer")
    public void shouldReturnOkWhenVolunteerAndNewsReleased(User volunteer) throws Exception {
        mockMvc.perform(get(ApiUrl.NEWS_PATH).param(TYPE_PARAM, RELEASED)
                .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + jwtFactory.generateTokenWithSpecifiedExpirationTime(volunteer, expirationTime)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnOkWhenAnonymousAndGetNewsReleased() throws Exception {
        mockMvc.perform(get(ApiUrl.NEWS_PATH).param(TYPE_PARAM, RELEASED))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnForbiddenWhenAnonymousAndNewsAchieved() throws Exception {
        mockMvc.perform(get(ApiUrl.NEWS_PATH).param(TYPE_PARAM, ACHIEVED))
                .andExpect(status().isForbidden());
    }
}
