package com.intive.patronage.toz.util;

import com.intive.patronage.toz.users.UserDataProvider;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserView;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(DataProviderRunner.class)
public class ModelMapperTest {

    private static final String PASSWORD_HASH = "passwordHash";
    private static final String PASSWORD = "password";

    @Test
    @UseDataProvider(value = "getTozAdminUserModel", location = UserDataProvider.class)
    public void convertToView(final User user) throws Exception {
        UserView userView = ModelMapper.convertIdentifiableToView(user, UserView.class);
        assertThat(userView).isEqualToIgnoringGivenFields(user, PASSWORD);
    }

    @Test
    @UseDataProvider(value = "getTozAdminUserView", location = UserDataProvider.class)
    public void convertToModel(final UserView userView) throws Exception {
        User user = ModelMapper.convertIdentifiableToModel(userView, User.class);
        assertThat(user).isEqualToIgnoringGivenFields(userView, PASSWORD_HASH);
    }

}
