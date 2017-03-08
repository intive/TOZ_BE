package com.intive.patronage.toz.repository;

import com.intive.patronage.toz.model.OrganizationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationInfoRepository extends JpaRepository<OrganizationInfo, Long> {
    OrganizationInfo findByName(String name);
}
