package com.intive.patronage.toz.users.model.db;

import com.intive.patronage.toz.base.model.PersonalData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@NoArgsConstructor

public class User extends PersonalData {

    private String passwordHash;

    public User(String name) {
        this.name = name;
    }
}
