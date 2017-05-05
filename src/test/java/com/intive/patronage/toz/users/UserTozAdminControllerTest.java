package com.intive.patronage.toz.users;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.users.model.db.RoleEntity;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserView;
import com.intive.patronage.toz.util.ModelMapper;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.Collections;

import static com.intive.patronage.toz.users.UserDataProvider.EXPECTED_ID;
import static com.intive.patronage.toz.users.UserDataProvider.JSON_CONTENT_TYPE;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(DataProviderRunner.class)
public class UserTozAdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Before
    public void setUp() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        Collection<? extends GrantedAuthority> authorities =
                Collections.singleton(RoleEntity.buildWithRole(User.Role.TOZ));
        doReturn(authorities).when(authentication).getAuthorities();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        MockitoAnnotations.initMocks(this);
        PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
        UserTozAdminController userTozAdminController = new UserTozAdminController(userService, passwordEncoderMock);
        mockMvc = MockMvcBuilders.standaloneSetup(userTozAdminController).build();
    }

    @Test
    public void getAllUsers() throws Exception {
        mockMvc.perform(get(ApiUrl.USERS_PATH))
                .andExpect(status().isOk());

        verify(userService, times(1)).findAll();
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void getUserById() throws Exception {
        String requestUrl = String.format("%s/%s", ApiUrl.USERS_PATH, EXPECTED_ID);

        when(userService.findOneById(EXPECTED_ID)).thenReturn(any(User.class));
        mockMvc.perform(get(requestUrl))
                .andExpect(status().isOk());

        verify(userService, times(1)).findOneById(EXPECTED_ID);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @UseDataProvider(value = "getUserWithView", location = UserDataProvider.class)
    public void createUser(final User user, final UserView userView) throws Exception {
        final String userViewJsonString = ModelMapper.convertToJsonString(userView);
        when(userService.createWithPasswordHash(any(User.class), any(String.class))).thenReturn(user);
        mockMvc.perform(post(ApiUrl.USERS_PATH)
                .contentType(JSON_CONTENT_TYPE)
                .content(userViewJsonString))
                .andExpect(status().isCreated());

        verify(userService, times(1)).createWithPasswordHash(any(User.class), any(String.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deleteUser() throws Exception {
        doNothing().when(userService).delete(EXPECTED_ID);
        String requestUrl = String.format("%s/%s", ApiUrl.USERS_PATH, EXPECTED_ID);
        mockMvc.perform(delete(requestUrl))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(EXPECTED_ID);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @UseDataProvider(value = "getUserWithView", location = UserDataProvider.class)
    public void updateUser(final User user, final UserView userView) throws Exception {
        final String userViewJsonString = ModelMapper.convertToJsonString(userView);

        when(userService.update(eq(EXPECTED_ID), any(User.class))).thenReturn(user);
        String requestUrl = String.format("%s/%s", ApiUrl.USERS_PATH, EXPECTED_ID);
        mockMvc.perform(put(requestUrl)
                .contentType(JSON_CONTENT_TYPE)
                .content(userViewJsonString))
                .andExpect(status().isOk());

        verify(userService, times(1)).update(eq(EXPECTED_ID), any(User.class));
        verifyNoMoreInteractions(userService);
    }

}
