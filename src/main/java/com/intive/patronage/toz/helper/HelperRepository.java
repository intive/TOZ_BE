package com.intive.patronage.toz.helper;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.helper.model.db.Helper;

import java.util.List;

public interface HelperRepository extends IdentifiableRepository<Helper> {
    List<Helper> findByCategory(Helper.Category category);
}
