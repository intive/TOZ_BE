package com.intive.patronage.toz.status;

import com.intive.patronage.toz.error.exception.AlreadyExistsException;
import com.intive.patronage.toz.status.model.PetStatus;
import com.intive.patronage.toz.util.RepositoryChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PetStatusService {

    private static final String PET_STATUS = "Pet status";
    private final PetsStatusRepository petsStatusRepository;

    @Autowired
    PetStatusService(PetsStatusRepository petsStatusRepository) {
        this.petsStatusRepository = petsStatusRepository;
    }

    public List<PetStatus> findAll() {
        return petsStatusRepository.findAll();
    }

    public PetStatus create(PetStatus petStatus) {
        throwAlreadyExistsIfStatusNameExists(petStatus.getName());
        return petsStatusRepository.save(petStatus);
    }

    public PetStatus update(UUID id, PetStatus petStatus) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, petsStatusRepository, PET_STATUS);
        petStatus.setId(id);
        return petsStatusRepository.save(petStatus);
    }

    public void delete(UUID id) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, petsStatusRepository, PET_STATUS);
        petsStatusRepository.delete(id);
    }

    private void throwAlreadyExistsIfStatusNameExists(String statusName){
        if (petsStatusRepository.existsStatusByName(statusName)) {
            throw new AlreadyExistsException(PET_STATUS);
        }
    }
}
