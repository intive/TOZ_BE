package com.intive.patronage.toz.users;


import com.intive.patronage.toz.error.exception.ActivationExpiredException;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.error.exception.UserAlreadyActivatedException;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.db.UserActivation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class UserActivationService {

    private final static String USER_ACTIVATION = "User Activation";
    private final static String USER = "User";
    private final static String EXPIRED = "Expired user activation";
    private final static String ALREADY_ACTIVATED = "User already activated";
    private final UserRepository userRepository;

    @Value("${expiration.user_activation.time}")
    private Long expiration;

    private UserActivationRepository userActivationRepository;

    private void throwNotFoundExceptionIfNotExists(final UUID id, final String name) {
        if (!userActivationRepository.exists(id)) {
            throw new NotFoundException(name);
        }
    }
    @Autowired
    public UserActivationService(UserActivationRepository userActivationRepository, UserRepository userRepository){
        this.userRepository = userRepository;
        this.userActivationRepository = userActivationRepository;
    }

    public UserActivation createUserActivation(){
        UserActivation userActivation = new UserActivation();
        Date date = new Date();
        userActivation.setExpiredDate(new Date(date.getTime() + expiration));
        userActivation.setIsActivated(false);
        userActivationRepository.save(userActivation);
        return userActivation;
    }
    public Boolean checkUserActivation(UUID id) throws ActivationExpiredException{
        throwNotFoundExceptionIfNotExists(id,USER_ACTIVATION);
        UserActivation userActivation = userActivationRepository.findOne(id);

        if (userActivation.getIsActivated()){
            throw new UserAlreadyActivatedException(ALREADY_ACTIVATED);
        }
        if (userActivation.getCreated().after(userActivation.getExpiredDate())){
            throw new ActivationExpiredException(EXPIRED);
        }

        return true;
    }

    @Transactional
    public UserActivation assignUserToUserActivation(UUID idUser, UUID idUserActivation){
        throwNotFoundExceptionIfNotExists(idUser,USER_ACTIVATION);
        throwNotFoundExceptionIfNotExists(idUserActivation,USER_ACTIVATION);

        User user = userRepository.findOne(idUser);
        UserActivation userActivation = userActivationRepository.findOne(idUserActivation);

        userActivation.setIsActivated(true);
        userActivation.setUser(user);
        userActivationRepository.save(userActivation);
        return userActivation;
    }

    public void removeUserActivation(UUID id){
        throwNotFoundExceptionIfNotExists(id, USER_ACTIVATION);
        userActivationRepository.delete(id);
    }
}
