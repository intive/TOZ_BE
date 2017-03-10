package com.intive.patronage.toz.repository;

import com.intive.patronage.toz.model.db.OrganizationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationInfoRepository extends JpaRepository<OrganizationInfo, UUID> {
}
