package com.intive.patronage.toz.helper;

import com.intive.patronage.toz.helper.model.db.Helper;
import com.intive.patronage.toz.util.RepositoryChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HelperService {
    private final static String HELPER = "Helper";
    private final HelperRepository helperRepository;

    @Autowired
    HelperService(HelperRepository helperRepository) {
        this.helperRepository = helperRepository;
    }

    List<Helper> findAllHelpers(final Helper.Category category) {
        if (category != null) {
            return helperRepository.findByCategory(category);
        }
        return helperRepository.findAll();
    }

    Helper findById(final UUID id) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, helperRepository, HELPER);
        return helperRepository.findOne(id);
    }

    Helper createHelper(Helper helper) {
        return helperRepository.save(helper);
    }

    void deleteHelper(UUID id) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, helperRepository, HELPER);
        helperRepository.delete(id);
    }

    Helper updateHelper(UUID id, Helper helper) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, helperRepository, HELPER);
        helper.setId(id);
        return helperRepository.save(helper);
    }
}
