package com.intive.patronage.toz.users;


import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.users.model.db.User;

public interface UserRepository extends IdentifiableRepository<User> {
    User findByEmail(String email);

    User findByName(String name);

    boolean existsByEmail(String email);

    boolean existsByName(String name);
}
