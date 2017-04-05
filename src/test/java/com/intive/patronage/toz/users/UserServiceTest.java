package com.intive.patronage.toz.users;


import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.users.model.db.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@RunWith(SpringJUnit4ClassRunner.class)

public class UserServiceTest {
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Johny";
    private static final String EXPECTED_PASSWORD = "secret";
    private static final String EXPECTED_SURNAME = "Bravo";
    private static final User.Role EXPECTED_ROLE = User.Role.TOZ;

    private User user;
    private UUID userId;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Before
    public void setUp() throws Exception { // TODO: data provider
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository);
        user = new User();
        user.setId(EXPECTED_ID);
        user.setName(EXPECTED_NAME);
        user.setPassword(EXPECTED_PASSWORD);
        user.setSurname(EXPECTED_SURNAME);
        user.setRole(EXPECTED_ROLE);
        userId = user.getId();
    }

    @Test
    public void findAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> users = userService.findAll();
        assertTrue(users.isEmpty());
    }

    @Test
    public void findById() throws Exception {
        when(userRepository.exists(userId)).thenReturn(true);
        when(userRepository.findOne(userId)).thenReturn(user);

        User dbUser = userService.findOneById(userId);
        assertEquals(EXPECTED_NAME, dbUser.getName());
        assertEquals(EXPECTED_PASSWORD, dbUser.getPassword());
        assertEquals(EXPECTED_SURNAME, dbUser.getSurname());
        assertEquals(EXPECTED_ROLE, dbUser.getRole());

        verify(userRepository, times(1)).exists(eq(userId));
        verify(userRepository, times(1)).findOne(eq(userId));
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdNotFoundException() throws Exception {
        when(userRepository.exists(userId)).thenReturn(false);
        userService.findOneById(userId);

        verify(userRepository, times(1)).exists(eq(userId));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void createUser() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User createdUser = userService.create(user);
        assertEquals(EXPECTED_NAME, createdUser.getName());
        assertEquals(EXPECTED_PASSWORD, createdUser.getPassword());
        assertEquals(EXPECTED_SURNAME, createdUser.getSurname());
        assertEquals(EXPECTED_ROLE, createdUser.getRole());
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void deleteUser() throws Exception {
        when(userRepository.exists(userId)).thenReturn(true);
        userService.delete(userId);

        verify(userRepository, times(1)).exists(eq(userId));
        verify(userRepository, times(1)).delete(eq(userId));
        verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = NotFoundException.class)
    public void deleteUserNotFoundException() throws Exception {
        when(userRepository.exists(userId)).thenReturn(false);
        userService.delete(userId);

        verify(userRepository, times(1)).exists(eq(userId));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void updateUser() throws Exception {
        when(userRepository.exists(userId)).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(user);
        User savedUser = userService.update(userId, user);

        assertEquals(EXPECTED_NAME, savedUser.getName());
        assertEquals(EXPECTED_PASSWORD, savedUser.getPassword());
        assertEquals(EXPECTED_SURNAME, savedUser.getSurname());
        assertEquals(EXPECTED_ROLE, savedUser.getRole());
    }

    @Test(expected = NotFoundException.class)
    public void updateUserNotFoundException() throws Exception {
        when(userRepository.exists(userId)).thenReturn(false);
        userService.update(userId, user);

        verify(userRepository, times(1)).exists(eq(userId));
        verifyNoMoreInteractions(userRepository);
    }

}
