package com.intive.patronage.toz.status;

import com.intive.patronage.toz.error.exception.AlreadyExistsException;
import com.intive.patronage.toz.status.model.PetsStatus;
import com.intive.patronage.toz.util.RepositoryChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PetsStatusService {

    private static final String PETS_STATUS = "Pets status";
    private final PetsStatusRepository petsStatusRepository;

    @Autowired
    PetsStatusService(PetsStatusRepository petsStatusRepository) {
        this.petsStatusRepository = petsStatusRepository;
    }

    public List<PetsStatus> findAll() {
        return petsStatusRepository.findAll();
    }

    public PetsStatus create(PetsStatus petsStatus) {
        throwNotFoundExceptionIfStatusNameExists(petsStatus.getName());
        return petsStatusRepository.save(petsStatus);
    }

    public PetsStatus update(UUID id, PetsStatus petsStatus) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, petsStatusRepository, PETS_STATUS);
        petsStatus.setId(id);
        return petsStatusRepository.save(petsStatus);
    }

    public void delete(UUID id) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, petsStatusRepository, PETS_STATUS);
        petsStatusRepository.delete(id);
    }

    private void throwNotFoundExceptionIfStatusNameExists(String email){
        if (petsStatusRepository.existsStatusByName(email)) {
            throw new AlreadyExistsException(PETS_STATUS);
        }
    }


}
