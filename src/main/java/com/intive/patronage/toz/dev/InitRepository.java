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
                Pet pet = new Pet();
                pet.setName("Name:" + i);
                Pet.Type[] types = Pet.Type.values();
                Pet.Sex[] sexes = Pet.Sex.values();
                pet.setType(types[i % types.length]);
                pet.setSex(sexes[i % sexes.length]);
                pet.setDescription("description:" + i);
                pet.setAddress("address:" + i);
                petsRepository.save(pet);

                News news = new News();
                news.setTitle("Title:" + i);
                News.Type[] newsTypes = News.Type.values();
                news.setType(newsTypes[i % newsTypes.length]);
                news.setContents("contents:" + i);
                news.setPhotoUrl("photo:" + i);
                newsRepository.save(news);
            }
        };
    }
}
