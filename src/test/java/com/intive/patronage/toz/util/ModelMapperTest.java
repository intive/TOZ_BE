package com.intive.patronage.toz.util;

import com.intive.patronage.toz.users.UserDataProvider;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserView;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.intive.patronage.toz.users.UserDataProvider.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(DataProviderRunner.class)
public class ModelMapperTest {

    @Test
    @UseDataProvider(value = "getTozAdminUserModel", location = UserDataProvider.class)
    public void convertToView(final User user) throws Exception {
        UserView userView = ModelMapper.convertToView(user, UserView.class);
        assertThat(userView.getId()).isEqualTo(EXPECTED_ID);
        assertThat(userView.getName()).isEqualTo(EXPECTED_NAME);
        assertThat(userView.getPassword()).isNull();
        assertThat(userView.getSurname()).isEqualTo(EXPECTED_SURNAME);
        assertThat(userView.getPhoneNumber()).isEqualTo(EXPECTED_PHONE_NUMBER);
        assertThat(userView.getEmail()).isEqualTo(EXPECTED_EMAIL);
        assertThat(userView.getRoles()).containsOnly(TOZ_ROLE);
    }

    @Test
    @UseDataProvider(value = "getTozAdminUserView", location = UserDataProvider.class)
    public void convertToModel(final UserView userView) throws Exception {
        User user = ModelMapper.convertToModel(userView, User.class);
        assertThat(user.getId()).isEqualTo(EXPECTED_ID);
        assertThat(user.getName()).isEqualTo(EXPECTED_NAME);
        assertThat(user.getPasswordHash()).isNull();
        assertThat(user.getSurname()).isEqualTo(EXPECTED_SURNAME);
        assertThat(user.getPhoneNumber()).isEqualTo(EXPECTED_PHONE_NUMBER);
        assertThat(user.getEmail()).isEqualTo(EXPECTED_EMAIL);
        assertThat(user.getRoles()).containsOnly(TOZ_ROLE);
    }

}
