package com.intive.patronage.toz.users.model.db;

import com.intive.patronage.toz.base.model.PersonalData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User extends PersonalData {

    private String passwordHash;


}
