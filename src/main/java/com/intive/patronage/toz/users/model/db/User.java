package com.intive.patronage.toz.users.model.db;

import com.intive.patronage.toz.base.model.PersonalData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends PersonalData {

    private String passwordHash;

    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordChangeDate;


    public User(String name) {
        this.name = name;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        passwordChangeDate = new Date();
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", passwordChangeDate=" + passwordChangeDate +
                ", roles=" + roles +
                '}';
    }
}
