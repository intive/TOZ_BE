package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.Application;
import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.news.NewsRepository;
import com.intive.patronage.toz.news.model.db.News;
import com.intive.patronage.toz.pet.PetsRepository;
import com.intive.patronage.toz.pet.model.db.Pet;
import com.intive.patronage.toz.users.model.db.Role;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.intive.patronage.toz.pet.model.db.Pet.Sex.MALE;
import static com.intive.patronage.toz.pet.model.db.Pet.Type.DOG;
import static com.intive.patronage.toz.tokens.TokenControllerTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(DataProviderRunner.class)
@ContextConfiguration(classes = Application.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(
        properties = {
                ApiProperties.JWT_SECRET_BASE64,
                ApiProperties.SUPER_ADMIN_PASSWORD
        }
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
    private static final Role ROLE_VOLUNTEER = Role.VOLUNTEER;

    @Value("${jwt.expiration-time-minutes}")
    private long expirationTime;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private PetsRepository petsRepository;
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

    @DataProvider
    public static Object[] getNewsAchieved() {
        News news = new News();
        news.setContents("string");
        news.setTitle("string");
        news.setType(News.Type.ARCHIVED);
        return new News[]{news};
    }

    @DataProvider
    public static Object[] getNewsReleased() {
        News news = new News();
        news.setContents("string");
        news.setTitle("string");
        news.setType(News.Type.RELEASED);
        return new News[]{news};
    }

    @DataProvider
    public static Object[] getCompletePet() {
        Pet pet = new Pet();
        pet.setType(DOG);
        pet.setSex(MALE);
        pet.setName("string");
        return new Pet[]{pet};
    }

    @Test
    @UseDataProvider("getAdmin")
    public void shouldReturnOkWhenAdminAndGetNewsReleased(User admin) throws Exception {
        mockMvc.perform(get(ApiUrl.NEWS_PATH).param(TYPE_PARAM, RELEASED)
                .header(AUTHORIZATION_HEADER,
                        String.format("%s%s", TOKEN_PREFIX, jwtFactory.generateToken(admin, expirationTime))))
                .andExpect(status().isOk());
    }

    @Test
    @UseDataProvider("getVolunteer")
    public void shouldReturnForbiddenWhenVolunteerAndNewsAchieved(User volunteer) throws Exception {
        mockMvc.perform(get(ApiUrl.NEWS_PATH).param(TYPE_PARAM, ACHIEVED)
                .header(AUTHORIZATION_HEADER,
                        String.format("%s%s", TOKEN_PREFIX, jwtFactory.generateToken(volunteer, expirationTime))))
                .andExpect(status().isForbidden());
    }

    @Test
    @UseDataProvider("getVolunteer")
    public void shouldReturnOkWhenVolunteerAndNewsReleased(User volunteer) throws Exception {
        mockMvc.perform(get(ApiUrl.NEWS_PATH).param(TYPE_PARAM, RELEASED)
                .header(AUTHORIZATION_HEADER,
                        String.format("%s%s", TOKEN_PREFIX, jwtFactory.generateToken(volunteer, expirationTime))))
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

    @Test
    @UseDataProvider("getNewsAchieved")
    public void shouldReturnForbiddenWhenAnonymousAndGetNewsAchievedById(News achievedNews) throws Exception {
        UUID id = newsRepository.save(achievedNews).getId();
        mockMvc.perform(get(String.format("%s/%s", ApiUrl.NEWS_PATH, id)))
                .andExpect(status().isForbidden());
    }

    @Test
    @UseDataProvider("getNewsReleased")
    public void shouldReturnOkWhenAnonymousAndGetNewsReleasedById(News releasedNews) throws Exception {
        UUID id = newsRepository.save(releasedNews).getId();
        mockMvc.perform(get(String.format("%s/%s", ApiUrl.NEWS_PATH, id)))
                .andExpect(status().isOk());
    }

    @Test
    @UseDataProvider("getCompletePet")
    public void shouldReturnOkWhenAnonymousAndGetPetWithCompleteFieldsById(Pet pet) throws Exception {
        UUID id = petsRepository.save(pet).getId();
        mockMvc.perform(get(String.format("%s/%s", ApiUrl.PETS_PATH, id)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnForbiddenWhenAnonymousAndGetPetWithIncompleteFieldsById() throws Exception {
        Pet incompletePet = new Pet();
        incompletePet.setType(DOG);
        UUID id = petsRepository.save(incompletePet).getId();
        mockMvc.perform(get(String.format("%s/%s", ApiUrl.PETS_PATH, id)))
                .andExpect(status().isForbidden());
    }

    @Test
    @UseDataProvider("getAdmin")
    public void shouldReturnOkWhenAdminAndGetPetWithIncompleteFieldsById(User admin) throws Exception {
        Pet incompletePet = new Pet();
        incompletePet.setType(DOG);
        UUID id = petsRepository.save(incompletePet).getId();
        mockMvc.perform(get(String.format("%s/%s", ApiUrl.PETS_PATH, id))
                .header(AUTHORIZATION_HEADER,
                        String.format("%s%s", TOKEN_PREFIX, jwtFactory.generateToken(admin, expirationTime))))
                .andExpect(status().isOk());
    }
}
