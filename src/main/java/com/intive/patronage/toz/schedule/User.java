package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.model.db.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends Identifiable {//simplified user mock, waits for users implementatio

    private String name;
    private String lastName;
    private String login;
    private String email;
}
