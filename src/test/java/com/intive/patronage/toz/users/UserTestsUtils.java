package com.intive.patronage.toz.users;

import com.intive.patronage.toz.users.model.db.Role;
import com.intive.patronage.toz.users.model.db.User;

import java.util.Collections;

public class UserTestsUtils {
    protected static User getUserWithRole(final Role role) {
        final User user = (User) UserDataProvider.getTozAdminUserModel()[0];
        user.setRoles(Collections.singleton(role));
        return user;
    }
}
