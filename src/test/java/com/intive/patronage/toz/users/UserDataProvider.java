package com.intive.patronage.toz.users;

import com.intive.patronage.toz.users.model.db.Role;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserView;
import com.tngtech.java.junit.dataprovider.DataProvider;
import org.springframework.http.MediaType;

import java.util.UUID;

public class UserDataProvider {

    static final int USERS_LIST_SIZE = 5;
    static final UUID EXPECTED_ID = UUID.randomUUID();
    static final String EXPECTED_NAME = "Johny";
    static final String EXPECTED_PASSWORD = "johnyPassword";
    static final String EXPECTED_PASSWORD_HASH = "a7sd6a7sd67asd";
    static final String EXPECTED_SURNAME = "Bravo";
    static final String EXPECTED_PHONE_NUMBER = "111222333";
    static final String EXPECTED_EMAIL = "johny.bravo@gmail.com";
    static final Role TOZ_ROLE = Role.TOZ;
    static final boolean EXPECTED_ACTIVE = true;
    static final MediaType JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @DataProvider
    public static Object[] getTozAdminUserModel() {
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
    public static Object[] getTozAdminUserView() {
        final UserView userView = new UserView();
        userView.setId(EXPECTED_ID);
        userView.setName(EXPECTED_NAME);
        userView.setPassword(EXPECTED_PASSWORD);
        userView.setSurname(EXPECTED_SURNAME);
        userView.setPhoneNumber(EXPECTED_PHONE_NUMBER);
        userView.setEmail(EXPECTED_EMAIL);
        userView.addRole(TOZ_ROLE);
        return new Object[]{userView};
    }

    @DataProvider
    public static Object[][] getTozUserWithView() {
        final User user = (User) getTozAdminUserModel()[0];
        final UserView userView = (UserView) getTozAdminUserView()[0];
        return new Object[][]{{user, userView}};
    }

    @DataProvider
    public static Object[] getSuperAdminUserModel() {
        final User user = (User) getTozAdminUserModel()[0];
        user.addRole(Role.SA);
        return new Object[]{user};
    }

}
