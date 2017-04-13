package com.intive.patronage.toz.util;

import com.intive.patronage.toz.news.NewsRepository;
import com.intive.patronage.toz.news.model.db.News;
import com.intive.patronage.toz.pet.PetsRepository;
import com.intive.patronage.toz.pet.model.db.Pet;
import com.intive.patronage.toz.users.UserRepository;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;


@Component
@Profile("dev")
class InitRepository {

    private static final Random NUMBER_GENERATOR = new Random();
    private static final int NUMBER_UPPER_BOUND = 999;

    @Autowired
    private PetsRepository petsRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            for (int i = 0; i < 5; i++) {
                final Pet pet = createPetWithValue(i);
                final Pet emptyPet = createPetWithEmptyFields(i);
                petsRepository.save(pet);
                petsRepository.save(emptyPet);

                final News news = createNewsWithValue(i);
                newsRepository.save(news);

                final User user = createUserWithValue(i);
                userRepository.save(user);
            }
        };
    }

    private static Pet createPetWithValue(int value) {
        Pet pet = new Pet();
        pet.setName("Name:" + value);
        Pet.Type[] types = Pet.Type.values();
        Pet.Sex[] sexes = Pet.Sex.values();
        pet.setType(types[value % types.length]);
        pet.setSex(sexes[value % sexes.length]);
        pet.setDescription("description:" + value);
        pet.setAddress("address:" + value);
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
        news.setTitle("Title:" + value);
        News.Type[] newsTypes = News.Type.values();
        news.setType(newsTypes[value % newsTypes.length]);
        news.setContents("contents:" + value);
        news.setPhotoUrl("photo:" + value);
        return news;
    }

    private static User createUserWithValue(int value) {
        final User user = new User();
        user.setName(String.format("name:%d", value));
        user.setSurname(String.format("surname:%d", value));
        user.setPasswordHash(String.format("passwordHash:%d", value));
        user.setEmail(String.format("user%d.email@gmail.com", value));
        user.setPhoneNumber(getRandomPhoneNumber());
        user.setRole(User.Role.VOLUNTEER);
        return user;
    }

    private static String getRandomPhoneNumber() {
        final int randomNumber = NUMBER_GENERATOR.nextInt(NUMBER_UPPER_BOUND);
        return String.format("111222%03d", randomNumber);
    }
}
