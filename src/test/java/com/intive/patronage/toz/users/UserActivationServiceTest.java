package com.intive.patronage.toz.users;

import com.intive.patronage.toz.users.model.db.User;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;

@RunWith(DataProviderRunner.class)
public class UserActivationServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserActivationService userActivationService;
    private static final String SECRET = "c2VjcmV0";

    private static final String USER_EMAIL = "email@email.com";
    private static final String USER_FORENAME = "firstname";
    private static final String USER_SURNAME = "lastname";
    private static final long EXPIRATION_TIME = 100000;
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userActivationService = new UserActivationService(userRepository, EXPIRATION_TIME, SECRET );
    }

    @DataProvider
    public static Object[] getProperUser() {
        User user = new User();
        user.setEmail(USER_EMAIL);
        user.setForename(USER_FORENAME);
        user.setSurname(USER_SURNAME);
        return new User[]{user};
    }

    @Test
    @UseDataProvider("getProperUser")
    public void generateActivationToken(final User user) throws Exception {
        assertTrue(userActivationService.generateUserActivationToken(user) != null);
    }

    @Test
    @UseDataProvider("getProperUser")
    public void checkCorrectUserActivation(final User user) throws Exception {

        String token = userActivationService.generateUserActivationToken(user);
        User userToCompare = userActivationService.checkActivationToken(token);

        assertTrue(userToCompare.getEmail().equals(USER_EMAIL));
        assertTrue(userToCompare.getSurname().equals(USER_SURNAME));
        assertTrue(userToCompare.getForename().equals(USER_FORENAME));
    }

}
