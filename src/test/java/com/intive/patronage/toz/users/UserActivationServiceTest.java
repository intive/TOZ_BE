package com.intive.patronage.toz.users;

import com.intive.patronage.toz.error.exception.ActivationExpiredException;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.users.model.db.UserActivation;
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
import sun.rmi.server.Activation;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(DataProviderRunner.class)
public class UserActivationServiceTest {
    @Mock
    private UserActivationRepository userActivationRepository;
    @Mock
    private UserRepository userRepository;

    private UserActivationService userActivationService;

    private static final Date CURRENT_DATE = new Date(1491769126000L);
    private static final UUID CORRECT_EXPECTED_ID = UUID.randomUUID();
    private static final Date CORRECT_EXPECTED_CREATED_DATE = new Date(1491769127000L);
    private static final UUID EXPIRED_EXPECTED_ID = UUID.randomUUID();
    private static final Date EXPIRED_EXPECTED_CREATED_DATE = new Date(1492879497000L);

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userActivationService = new UserActivationService(userActivationRepository, userRepository);
        System.out.println(EXPIRED_EXPECTED_CREATED_DATE);

    }

    @DataProvider
    public static Object[] getProperUserActivation() {
        UserActivation correctUserActivation = new UserActivation();
        correctUserActivation.setId(CORRECT_EXPECTED_ID);
        correctUserActivation.setCreated(CURRENT_DATE);
        correctUserActivation.setExpiredDate(CORRECT_EXPECTED_CREATED_DATE);
        correctUserActivation.setIsActivated(false);
        return new UserActivation[]{correctUserActivation};
    }

    @DataProvider
    public static Object[] getProperExpiredUserActivation() {
        UserActivation expiredUserActivation = new UserActivation();
        expiredUserActivation.setId(EXPIRED_EXPECTED_ID);
        expiredUserActivation.setCreated(CURRENT_DATE);
        expiredUserActivation.setExpiredDate(EXPIRED_EXPECTED_CREATED_DATE);
        expiredUserActivation.setIsActivated(false);
        return new UserActivation[]{expiredUserActivation};
    }


    @UseDataProvider("getProperExpiredUserActivation")
    public void checkActivationExpiredException(final UserActivation userActivation) throws Exception {
        final UUID userActivationId = userActivation.getId();
        when(userActivationRepository.exists(userActivationId)).thenReturn(true);
        when(userActivationRepository.findOne(userActivationId)).thenReturn(userActivation);

        exception.expect(ActivationExpiredException.class);
        userActivationService.checkUserActivation(EXPIRED_EXPECTED_ID);

    }


    @Test
    @UseDataProvider("getProperUserActivation")
    public void checkCorrectUserActivation(final UserActivation correctUserActivation) throws Exception {
        when(userActivationRepository.exists(CORRECT_EXPECTED_ID)).thenReturn(true);
        when(userActivationRepository.findOne(CORRECT_EXPECTED_ID)).thenReturn(correctUserActivation);

        assertTrue(userActivationService.checkUserActivation(CORRECT_EXPECTED_ID));

        verify(userActivationRepository, times(1)).exists(eq(CORRECT_EXPECTED_ID));
    }


    @Test
    public void removeUserActivation() throws Exception {
        final UUID userActivationId = UUID.randomUUID();
        when(userActivationRepository.exists(userActivationId)).thenReturn(true);
        userActivationService.removeUserActivation(userActivationId);

        verify(userActivationRepository, times(1)).exists(eq(userActivationId));
        verify(userActivationRepository, times(1)).delete(eq(userActivationId));
        verifyNoMoreInteractions(userActivationRepository);
    }

}
