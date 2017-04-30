package com.intive.patronage.toz.users;


import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.users.model.db.User;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;


@RunWith(DataProviderRunner.class)
public class UserServiceTest {
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Johny";
    private static final String EXPECTED_PASSWORD_HASH = "a7sd6a7sd67asd";
    private static final String EXPECTED_SURNAME = "Bravo";
    private static final User.Role EXPECTED_ROLE = User.Role.TOZ;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository);
    }

    @DataProvider
    public static Object[] getProperUser() {
        final User user = new User();
        user.setId(EXPECTED_ID);
        user.setName(EXPECTED_NAME);
        user.setPasswordHash(EXPECTED_PASSWORD_HASH);
        user.setSurname(EXPECTED_SURNAME);
        user.addRole(EXPECTED_ROLE);
        return new User[]{user};
    }

    @Test
    public void findAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        final List<User> users = userService.findAll();
        assertTrue(users.isEmpty());
    }

    @Test
    @UseDataProvider("getProperUser")
    public void findById(final User user) throws Exception {
        when(userRepository.exists(EXPECTED_ID)).thenReturn(true);
        when(userRepository.findOne(EXPECTED_ID)).thenReturn(user);

        final User dbUser = userService.findOneById(EXPECTED_ID);
        assertEquals(EXPECTED_NAME, dbUser.getName());
        assertEquals(EXPECTED_PASSWORD_HASH, dbUser.getPasswordHash());
        assertEquals(EXPECTED_SURNAME, dbUser.getSurname());
        assertTrue(dbUser.hasRole(EXPECTED_ROLE));

        verify(userRepository, times(1)).exists(eq(EXPECTED_ID));
        verify(userRepository, times(1)).findOne(eq(EXPECTED_ID));
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdNotFoundException() throws Exception {
        when(userRepository.exists(EXPECTED_ID)).thenReturn(false);
        userService.findOneById(EXPECTED_ID);

        verify(userRepository, times(1)).exists(eq(EXPECTED_ID));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @UseDataProvider("getProperUser")
    public void createUser(final User user) throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(user);

        final User createdUser = userService.createWithPasswordHash(user, EXPECTED_PASSWORD_HASH);
        assertEquals(EXPECTED_NAME, createdUser.getName());
        assertEquals(EXPECTED_PASSWORD_HASH, createdUser.getPasswordHash());
        assertEquals(EXPECTED_SURNAME, createdUser.getSurname());
        assertTrue(createdUser.hasRole(EXPECTED_ROLE));
        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).existsByEmail(any(String.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void deleteUser() throws Exception {
        when(userRepository.exists(EXPECTED_ID)).thenReturn(true);
        userService.delete(EXPECTED_ID);

        verify(userRepository, times(1)).exists(eq(EXPECTED_ID));
        verify(userRepository, times(1)).delete(eq(EXPECTED_ID));
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = NotFoundException.class)
    public void deleteUserNotFoundException() throws Exception {
        when(userRepository.exists(EXPECTED_ID)).thenReturn(false);
        userService.delete(EXPECTED_ID);

        verify(userRepository, times(1)).exists(eq(EXPECTED_ID));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @UseDataProvider("getProperUser")
    public void updateUser(final User user) throws Exception {
        when(userRepository.exists(EXPECTED_ID)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);
        final User savedUser = userService.update(EXPECTED_ID, user);

        assertEquals(EXPECTED_NAME, savedUser.getName());
        assertEquals(EXPECTED_PASSWORD_HASH, savedUser.getPasswordHash());
        assertEquals(EXPECTED_SURNAME, savedUser.getSurname());
        assertTrue(savedUser.hasRole(EXPECTED_ROLE));
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("getProperUser")
    public void updateUserNotFoundException(final User user) throws Exception {
        when(userRepository.exists(EXPECTED_ID)).thenReturn(false);
        userService.update(EXPECTED_ID, user);

        verify(userRepository, times(1)).exists(eq(EXPECTED_ID));
        verifyNoMoreInteractions(userRepository);
    }

}
