package com.intive.patronage.toz.users.model.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class RoleEntity { // TODO move to User

    @Id
    @Enumerated(EnumType.STRING)
    private User.Role role;
}
