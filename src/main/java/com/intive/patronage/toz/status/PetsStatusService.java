package com.intive.patronage.toz.status;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.status.model.PetsStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PetsStatusService {

    private static final String PETS_STATUS = "Pets status";
    private final PetsStatusRepository petsStatusRepository;

    @Autowired
    public PetsStatusService(PetsStatusRepository petsStatusRepository) {
        this.petsStatusRepository = petsStatusRepository;
    }

    public List<PetsStatus> findAll() {
        return petsStatusRepository.findAll();
    }

    public PetsStatus create(PetsStatus petsStatus) {
        return petsStatusRepository.save(petsStatus);
    }

    public PetsStatus update(UUID id, PetsStatus petsStatus) {
        throwNotFoundExceptionIfIdNotExists(id);
        petsStatus.setId(id);
        return petsStatusRepository.save(petsStatus);
    }

    public void delete(UUID id) {
        throwNotFoundExceptionIfIdNotExists(id);
        petsStatusRepository.delete(id);
    }

    void throwNotFoundExceptionIfIdNotExists(final UUID id) {
        if (!petsStatusRepository.exists(id)) {
            throw new NotFoundException(PETS_STATUS);
        }
    }
}
