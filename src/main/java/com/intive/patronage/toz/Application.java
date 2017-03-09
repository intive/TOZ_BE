package com.intive.patronage.toz;

import com.intive.patronage.toz.model.Pet;
import com.intive.patronage.toz.repository.PetsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(PetsRepository petsRepository) {
        return args -> {
            for (int i = 0; i < 10; i++) {
                Pet pet = new Pet();
                pet.setName("Name:" + i);
                pet.setSex(Pet.Sex.values()[i%2]);
                pet.setType(Pet.Type.values()[i%2]);
                petsRepository.save(pet);
            }
        };
    }
}
