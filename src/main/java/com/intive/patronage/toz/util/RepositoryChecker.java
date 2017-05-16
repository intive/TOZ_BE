package com.intive.patronage.toz.util;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.error.exception.NotFoundException;

import java.util.UUID;

public final class RepositoryChecker {
    private RepositoryChecker() {
    }

    public static void throwNotFoundExceptionIfNotExists(UUID id, IdentifiableRepository repo, String entityName) {
        if (!repo.exists(id)) {
            throw new NotFoundException(entityName);
        }
    }
}
