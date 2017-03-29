package com.intive.patronage.toz.dev;

import com.intive.patronage.toz.model.db.News;
import com.intive.patronage.toz.model.db.Pet;
import com.intive.patronage.toz.repository.NewsRepository;
import com.intive.patronage.toz.repository.PetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("dev")
class InitRepository {

    @Autowired
    private PetsRepository petsRepository;
    @Autowired
    private NewsRepository newsRepository;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            for (int i = 0; i < 5; i++) {
                Pet pet = createPetWithValue(i);
                Pet emptyPet = createPetWithEmptyFields(i);
                petsRepository.save(pet);
                petsRepository.save(emptyPet);

                News news = createNewsWithValue(i);
                newsRepository.save(news);
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
}
