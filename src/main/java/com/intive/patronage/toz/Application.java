package com.intive.patronage.toz;

import com.intive.patronage.toz.model.db.Pet;
import com.intive.patronage.toz.repository.PetsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner initDatabase(PetsRepository petsRepository) {
        return args -> {
            for (int i = 0; i < 10; i++) {
                Pet pet = new Pet();
                pet.setName("Name:" + i);
                pet.setType(Pet.Type.values()[i%3]);
                pet.setSex(Pet.Sex.values()[i%2]);
                pet.setDescription("description:" + i);
                pet.setAddress("address:" + i);
                petsRepository.save(pet);
            }
        };
    }
}
