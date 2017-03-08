package com.intive.patronage.toz.service;

import com.intive.patronage.toz.model.OrganizationInfo;

public interface OrganizationService {
    OrganizationInfo findOrganizationInfo();

    OrganizationInfo createOrganizationInfo(OrganizationInfo info);

    OrganizationInfo deleteOrganizationInfo();

    OrganizationInfo updateOrganizationInfo(OrganizationInfo info);
}
