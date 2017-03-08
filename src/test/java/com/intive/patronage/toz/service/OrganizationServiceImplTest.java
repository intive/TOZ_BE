package com.intive.patronage.toz.service;

import com.intive.patronage.toz.exception.AlreadyExistsException;
import com.intive.patronage.toz.exception.ArgumentNotValidException;
import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.model.OrganizationInfo;
import com.intive.patronage.toz.repository.OrganizationInfoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OrganizationServiceImplTest {

    private final static Long ID = 1L;
    private final static String ORG_NAME = "Org";
    private final static String ACCOUNT = "63 1020 4795 0000 9402 0103 5419";

    @Mock
    private OrganizationInfoRepository repository;

    private OrganizationService service;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        service = new OrganizationServiceImpl(repository);
        repository.deleteAllInBatch();
    }

    @Test(expected = NotFoundException.class)
    public void findNonExistingInfoShouldThrowException() throws Exception {
        when(repository.exists(anyLong())).thenReturn(false);
        service.findOrganizationInfo();
    }

    @Test
    public void findShouldCallRepository() throws Exception {
        when(repository.exists(anyLong())).thenReturn(true);
        service.findOrganizationInfo();
        verify(repository).findOne(anyLong());
    }

    @Test(expected = AlreadyExistsException.class)
    public void createExistingInfoShouldThrowException() throws Exception {
        when(repository.exists(anyLong())).thenReturn(true);
        OrganizationInfo info = new OrganizationInfo.Builder()
                .setName(ORG_NAME)
                .setAccountNumber(ACCOUNT)
                .build();

        service.createOrganizationInfo(info);
    }

    @Test(expected = ArgumentNotValidException.class)
    public void createShouldThrowExceptionWhenIdIsGiven() throws Exception {
        when(repository.exists(anyLong())).thenReturn(true);
        OrganizationInfo info = new OrganizationInfo.Builder()
                .setId(ID)
                .setName(ORG_NAME)
                .setAccountNumber(ACCOUNT)
                .build();

        service.createOrganizationInfo(info);
    }

    @Test
    public void createShouldSaveToRepository() throws Exception {
        when(repository.exists(anyLong())).thenReturn(false);
        OrganizationInfo info = new OrganizationInfo.Builder()
                .setName(ORG_NAME)
                .setAccountNumber(ACCOUNT)
                .build();

        service.createOrganizationInfo(info);
        verify(repository).save(any(OrganizationInfo.class));
    }

    @Test(expected = NotFoundException.class)
    public void deleteNonExistingInfoShouldThrowException() throws Exception {
        when(repository.exists(anyLong())).thenReturn(false);
        service.deleteOrganizationInfo();
    }

    @Test
    public void deleteShouldDeleteFromRepository() throws Exception {
        when(repository.exists(anyLong())).thenReturn(true);
        service.deleteOrganizationInfo();
        verify(repository).delete(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void updateNonExistingInfoShouldThrowException() throws Exception {
        when(repository.exists(anyLong())).thenReturn(false);
        OrganizationInfo info = new OrganizationInfo.Builder()
                .setName(ORG_NAME)
                .setAccountNumber(ACCOUNT)
                .build();

        service.updateOrganizationInfo(info);
    }

    @Test(expected = ArgumentNotValidException.class)
    public void updateShouldThrowExceptionWhenIdIsGiven() throws Exception {
        when(repository.exists(anyLong())).thenReturn(false);
        OrganizationInfo info = new OrganizationInfo.Builder()
                .setId(ID)
                .setName(ORG_NAME)
                .setAccountNumber(ACCOUNT)
                .build();

        service.updateOrganizationInfo(info);
    }

    @Test
    public void updateShouldSaveToRepository() throws Exception {
        when(repository.exists(anyLong())).thenReturn(true);
        OrganizationInfo info = new OrganizationInfo.Builder()
                .setName(ORG_NAME)
                .setAccountNumber(ACCOUNT)
                .build();
        when(repository.findOne(anyLong())).thenReturn(info);

        service.updateOrganizationInfo(info);
        verify(repository).save(any(OrganizationInfo.class));
    }
}
