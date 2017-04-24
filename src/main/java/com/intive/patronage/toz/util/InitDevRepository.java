package com.intive.patronage.toz.util;

import com.intive.patronage.toz.news.NewsRepository;
import com.intive.patronage.toz.news.model.db.News;
import com.intive.patronage.toz.pet.PetsRepository;
import com.intive.patronage.toz.pet.model.db.Pet;
import com.intive.patronage.toz.users.UsersRepository;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;


@Component
@Profile("dev")
class InitDevRepository {

    private static final Random NUMBER_GENERATOR = new Random();
    private static final int NUMBER_UPPER_BOUND = 999;

    @Autowired
    private PetsRepository petsRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDevDatabase() {
        return args -> {
            for (int i = 0; i < 5; i++) {
                final Pet pet = createPetWithValue(i);
                final Pet emptyPet = createPetWithEmptyFields(i);
                petsRepository.save(pet);
                petsRepository.save(emptyPet);

                final News news = createNewsWithValue(i);
                newsRepository.save(news);

                final User volunteer = createUserWithRole(User.Role.VOLUNTEER, i);
                usersRepository.save(volunteer);

                final User toz = createUserWithRole(User.Role.TOZ, i);
                usersRepository.save(toz);
            }
        };
    }

    private static Pet createPetWithValue(int value) {
        Pet pet = new Pet();
        pet.setName(String.format("name_%s", value));
        Pet.Type[] types = Pet.Type.values();
        Pet.Sex[] sexes = Pet.Sex.values();
        pet.setType(types[value % types.length]);
        pet.setSex(sexes[value % sexes.length]);
        pet.setDescription(String.format("description_%s", value));
        pet.setAddress(String.format("address_%s", value));
        return pet;
    }

    private static Pet createPetWithEmptyFields(int value) {
        Pet pet = new Pet();
        Pet.Type[] types = Pet.Type.values();
        pet.setType(types[value % types.length]);
        return pet;
    }

    private static News createNewsWithValue(int value) {
        News news = new News();
        news.setTitle(String.format("title_%s", value));
        News.Type[] newsTypes = News.Type.values();
        news.setType(newsTypes[value % newsTypes.length]);
        news.setContents(String.format("contents_%s", value));
        news.setPhotoUrl(String.format("photo_%s", value));
        return news;
    }

    private User createUserWithRole(User.Role role, int value) {
        final String roleString = role.toString();
        final User user = new User();
        final String name = String.format("%s_name_%d", roleString, value);
        user.setName(name);
        user.setSurname(String.format("%s_surname_%d", roleString, value));
        user.setPasswordHash(passwordEncoder.encode(name));
        user.setEmail(String.format("%s_user%d.email@gmail.com", roleString, value));
        user.setPhoneNumber(getRandomPhoneNumber());
        user.addRole(role);
        return user;
    }

    private static String getRandomPhoneNumber() {
        final int randomNumber = NUMBER_GENERATOR.nextInt(NUMBER_UPPER_BOUND);
        return String.format("111222%03d", randomNumber);
    }
}
