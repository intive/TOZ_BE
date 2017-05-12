package com.intive.patronage.toz.users.model.db;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleTest {

    private static final String SA = "SA";
    private static final String TOZ = "TOZ";
    private static final String VOLUNTEER = "VOLUNTEER";
    private static final String ANONYMOUS = "ANONYMOUS";

    @Test
    public void getAuthority() {
        assertThat(User.Role.SA.getAuthority()).isEqualTo(SA);
        assertThat(User.Role.TOZ.getAuthority()).isEqualTo(TOZ);
        assertThat(User.Role.VOLUNTEER.getAuthority()).isEqualTo(VOLUNTEER);
        assertThat(User.Role.ANONYMOUS.getAuthority()).isEqualTo(ANONYMOUS);
    }

}
