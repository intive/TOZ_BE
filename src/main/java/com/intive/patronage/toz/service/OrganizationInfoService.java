package com.intive.patronage.toz.service;

import com.intive.patronage.toz.exception.AlreadyExistsException;
import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.model.db.OrganizationInfo;
import com.intive.patronage.toz.model.view.AddressView;
import com.intive.patronage.toz.model.view.BankAccountView;
import com.intive.patronage.toz.model.view.ContactView;
import com.intive.patronage.toz.model.view.OrganizationInfoView;
import com.intive.patronage.toz.repository.OrganizationInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationInfoService {

    private final static String ORGANIZATION = "Organization";

    private final UUID organizationId;
    private final OrganizationInfoRepository infoRepository;

    @Autowired
    OrganizationInfoService(OrganizationInfoRepository infoRepository) {
        this.infoRepository = infoRepository;

        if (infoRepository.findAll().isEmpty()) {
            organizationId = UUID.randomUUID();
        } else {
            organizationId = infoRepository.findAll().get(0).getId();
        }
    }

    public OrganizationInfoView findOrganizationInfo() {
        if (!infoRepository.exists(organizationId)) {
            throw new NotFoundException(ORGANIZATION);
        }

        final OrganizationInfo info = infoRepository.findOne(organizationId);
        return convertToView(info);
    }

    public OrganizationInfoView createOrganizationInfo(final OrganizationInfoView infoView) {
        if (infoRepository.exists(organizationId)) {
            throw new AlreadyExistsException(ORGANIZATION);
        }

        final OrganizationInfo newInfo = new OrganizationInfo.Builder(organizationId, infoView.getName())
                .setAddress(infoView.getAddress())
                .setContact(infoView.getContact())
                .setBankAccount(infoView.getBankAccount())
                .build();

        return convertToView(infoRepository.save(newInfo));
    }

    public OrganizationInfoView deleteOrganizationInfo() {
        if (!infoRepository.exists(organizationId)) {
            throw new NotFoundException(ORGANIZATION);
        }

        final OrganizationInfo info = infoRepository.findOne(organizationId);
        infoRepository.delete(organizationId);
        return convertToView(info);
    }

    public OrganizationInfoView updateOrganizationInfo(final OrganizationInfoView infoView) {
        if (!infoRepository.exists(organizationId)) {
            throw new NotFoundException(ORGANIZATION);
        }

        final OrganizationInfo oldInfo = infoRepository.findOne(organizationId);
        final OrganizationInfo updatedInfo = new OrganizationInfo.Builder(oldInfo.getId(), infoView.getName())
                .setAddress(infoView.getAddress())
                .setContact(infoView.getContact())
                .setBankAccount(infoView.getBankAccount())
                .build();

        return convertToView(infoRepository.save(updatedInfo));
    }

    private OrganizationInfoView convertToView(final OrganizationInfo info) {
        final AddressView address = new AddressView.Builder(info.getPostCode(), info.getCity(), info.getStreet())
                .setApartmentNumber(info.getApartmentNumber())
                .setHouseNumber(info.getHouseNumber())
                .setCountry(info.getCountry())
                .build();

        final ContactView contact = new ContactView.Builder()
                .setEmail(info.getEmail())
                .setFax(info.getFax())
                .setPhone(info.getPhone())
                .setWebsite(info.getWebsite())
                .build();

        final BankAccountView bankAccount = new BankAccountView.Builder(info.getBankAccountNumber())
                .setBankName(info.getBankName())
                .build();

        return new OrganizationInfoView.Builder(info.getName(), bankAccount)
                .setAddress(address)
                .setContact(contact)
                .build();
    }
}
