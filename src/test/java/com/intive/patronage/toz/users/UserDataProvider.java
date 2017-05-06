package com.intive.patronage.toz.users;

import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserView;
import com.intive.patronage.toz.util.ModelMapper;
import com.tngtech.java.junit.dataprovider.DataProvider;
import org.springframework.http.MediaType;

import java.util.UUID;

public class UserDataProvider {

    static final int USERS_LIST_SIZE = 5;
    static final UUID EXPECTED_ID = UUID.randomUUID();
    static final String EXPECTED_NAME = "Johny";
    private static final String EXPECTED_PASSWORD = "johnyPassword";
    static final String EXPECTED_PASSWORD_HASH = "a7sd6a7sd67asd";
    static final String EXPECTED_SURNAME = "Bravo";
    static final String EXPECTED_PHONE_NUMBER = "111222333";
    static final String EXPECTED_EMAIL = "johny.bravo@gmail.com";
    static final User.Role TOZ_ROLE = User.Role.TOZ;
    static final User.Role SA_ROLE = User.Role.SA;
    static final MediaType JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;


    @DataProvider
    public static Object[] getTozAdminUser() {
        final User user = new User();
        user.setId(EXPECTED_ID);
        user.setName(EXPECTED_NAME);
        user.setPasswordHash(EXPECTED_PASSWORD_HASH);
        user.setSurname(EXPECTED_SURNAME);
        user.setPhoneNumber(EXPECTED_PHONE_NUMBER);
        user.setEmail(EXPECTED_EMAIL);
        user.addRole(TOZ_ROLE);
        return new Object[]{user};
    }

    @DataProvider
    public static Object[][] getUserWithView() {
        final User user = (User) getTozAdminUser()[0];
        final UserView userView = ModelMapper.convertToView(user, UserView.class);
        userView.setPassword(EXPECTED_PASSWORD);
        return new Object[][]{{user, userView}};
    }

    @DataProvider
    public static Object[] getSuperAdminUser() {
        final User user = (User) getTozAdminUser()[0];
        user.addRole(SA_ROLE);
        return new Object[]{user};
    }

}
