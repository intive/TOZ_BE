package com.intive.patronage.toz.schedule;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class UserService {
    //TODO: remove when proper service is implemented

    UserView findUser(UUID userUuid){
        return new UserView();
    }
}
