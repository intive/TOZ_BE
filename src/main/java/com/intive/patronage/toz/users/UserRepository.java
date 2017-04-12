package com.intive.patronage.toz.users;


import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.users.model.db.User;

public interface UserRepository extends IdentifiableRepository<User> {
    User findByEmail(String email);

    boolean existsByEmail(String email);
}
