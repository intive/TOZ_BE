package com.intive.patronage.toz.util;

import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserView;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(DataProviderRunner.class)
public class ModelMapperTest {

    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Johny";
    private static final String EXPECTED_SURNAME = "Bravo";
    private static final String EXPECTED_PHONE_NUMBER = "123123123";
    private static final User.Role TOZ_ROLE = User.Role.TOZ;
    private static final User.Role SA_ROLE = User.Role.SA;

    @DataProvider
    public static Object[] getUserModel() { // TODO location data provider
        User user = new User();
        user.setId(EXPECTED_ID);
        user.setName(EXPECTED_NAME);
        user.setSurname(EXPECTED_SURNAME);
        user.setPhoneNumber(EXPECTED_PHONE_NUMBER);
        user.addRole(TOZ_ROLE);
        user.addRole(SA_ROLE);
        return new Object[]{user};
    }

    @DataProvider
    public static Object[] getUserView() {
        UserView userView = new UserView();
        userView.setId(EXPECTED_ID);
        userView.setName(EXPECTED_NAME);
        userView.setSurname(EXPECTED_SURNAME);
        userView.setPhoneNumber(EXPECTED_PHONE_NUMBER);
        userView.addRole(TOZ_ROLE);
        userView.addRole(SA_ROLE);
        return new Object[]{userView};
    }

    @Test
    @UseDataProvider("getUserModel")
    public void convertToView(final User user) throws Exception {
        UserView userView = ModelMapper.convertToView(user, UserView.class);
        assertThat(userView.getId()).isEqualTo(EXPECTED_ID);
        assertThat(userView.getName()).isEqualTo(EXPECTED_NAME);
        assertThat(userView.getPassword()).isNull();
        assertThat(userView.getSurname()).isEqualTo(EXPECTED_SURNAME);
        assertThat(userView.getPhoneNumber()).isEqualTo(EXPECTED_PHONE_NUMBER);
        assertThat(userView.getRoles()).containsOnly(TOZ_ROLE, SA_ROLE);
    }

    @Test
    @UseDataProvider("getUserView")
    public void convertToModel(final UserView userView) throws Exception {
        User user = ModelMapper.convertToModel(userView, User.class);
        assertThat(user.getId()).isEqualTo(EXPECTED_ID);
        assertThat(user.getName()).isEqualTo(EXPECTED_NAME);
        assertThat(user.getPasswordHash()).isNull();
        assertThat(user.getSurname()).isEqualTo(EXPECTED_SURNAME);
        assertThat(user.getPhoneNumber()).isEqualTo(EXPECTED_PHONE_NUMBER);
        assertThat(user.getRoles()).containsOnly(TOZ_ROLE, SA_ROLE);
    }

}
