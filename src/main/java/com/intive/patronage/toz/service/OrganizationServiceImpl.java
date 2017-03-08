package com.intive.patronage.toz.service;

import com.intive.patronage.toz.exception.AlreadyExistsException;
import com.intive.patronage.toz.exception.ArgumentNotValidException;
import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.model.OrganizationInfo;
import com.intive.patronage.toz.repository.OrganizationInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final static Long ORGANIZATION_ID = 1L;
    private final static String ORGANIZATION = "Organization";

    private OrganizationInfoRepository repository;

    @Autowired
    public OrganizationServiceImpl(OrganizationInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public OrganizationInfo findOrganizationInfo() {
        if (!repository.exists(ORGANIZATION_ID)) {
            throw new NotFoundException(ORGANIZATION);
        }

        return repository.findOne(ORGANIZATION_ID);
    }

    @Override
    public OrganizationInfo createOrganizationInfo(OrganizationInfo info) {
        if (info.getId() != null) {
            throw new ArgumentNotValidException("id");
        }

        if (repository.exists(ORGANIZATION_ID)) {
            throw new AlreadyExistsException(ORGANIZATION);
        }

        OrganizationInfo newInfo = new OrganizationInfo.Builder()
                .setId(ORGANIZATION_ID)
                .setName(info.getName())
                .setCity(info.getCity())
                .setPostCode(info.getPostCode())
                .setStreet(info.getStreet())
                .setAccountNumber(info.getAccountNumber())
                .setBankName(info.getBankName())
                .setEmail(info.getEmail())
                .setFax(info.getFax())
                .setPhone(info.getPhone())
                .setWebsite(info.getWebsite())
                .build();

        return repository.save(newInfo);
    }

    @Override
    public OrganizationInfo deleteOrganizationInfo() {
        if (!repository.exists(ORGANIZATION_ID)) {
            throw new NotFoundException(ORGANIZATION);
        }

        OrganizationInfo info = repository.getOne(ORGANIZATION_ID);
        repository.delete(ORGANIZATION_ID);
        return info;
    }

    @Override
    public OrganizationInfo updateOrganizationInfo(OrganizationInfo info) {
        if (info.getId() != null) {
            throw new ArgumentNotValidException("id");
        }

        if (!repository.exists(ORGANIZATION_ID)) {
            throw new NotFoundException(ORGANIZATION);
        }

        OrganizationInfo oldInfo = repository.findOne(ORGANIZATION_ID);
        OrganizationInfo updatedInfo = new OrganizationInfo.Builder()
                .setId(oldInfo.getId())
                .setName(info.getName())
                .setCity(info.getCity())
                .setPostCode(info.getPostCode())
                .setStreet(info.getStreet())
                .setAccountNumber(info.getAccountNumber())
                .setBankName(info.getBankName())
                .setEmail(info.getEmail())
                .setFax(info.getFax())
                .setPhone(info.getPhone())
                .setWebsite(info.getWebsite())
                .build();

        return repository.save(updatedInfo);
    }
}
