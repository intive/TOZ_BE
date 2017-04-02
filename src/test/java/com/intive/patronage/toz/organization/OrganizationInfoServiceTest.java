package com.intive.patronage.toz.organization;

import com.intive.patronage.toz.error.exception.AlreadyExistsException;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.organization.model.db.OrganizationInfo;
import com.intive.patronage.toz.organization.model.view.BankAccountView;
import com.intive.patronage.toz.organization.model.view.OrganizationInfoView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OrganizationInfoServiceTest {

    private final static String ORG_NAME = "Org";
    private final static String ACCOUNT = "63102047950000940201035419";
    private final static UUID ID = UUID.randomUUID();

    @Mock
    private OrganizationInfoRepository infoRepository;

    private OrganizationInfoService infoService;
    private OrganizationInfo info;
    private OrganizationInfoView infoView;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        infoService = new OrganizationInfoService(infoRepository);
        infoRepository.deleteAllInBatch();

        info = new OrganizationInfo.Builder(ID, ORG_NAME)
                .build();

        BankAccountView bankAccountView = new BankAccountView.Builder(ACCOUNT)
                .build();
        infoView = new OrganizationInfoView.Builder(ORG_NAME, bankAccountView)
                .build();
    }

    @Test(expected = NotFoundException.class)
    public void findNonExistingInfoShouldThrowException() throws Exception {
        when(infoRepository.exists(ID)).thenReturn(false);

        infoService.findOrganizationInfo();
    }

    @Test
    public void findShouldCallRepository() throws Exception {
        when(infoRepository.exists(any(UUID.class))).thenReturn(true);
        when(infoRepository.findOne(any(UUID.class))).thenReturn(info);

        infoService.findOrganizationInfo();

        verify(infoRepository).findOne(any(UUID.class));
    }

    @Test(expected = AlreadyExistsException.class)
    public void createExistingInfoShouldThrowException() throws Exception {
        when(infoRepository.exists(any(UUID.class))).thenReturn(true);

        infoService.createOrganizationInfo(infoView);
    }

    @Test
    public void createShouldSaveToRepository() throws Exception {
        when(infoRepository.exists(any(UUID.class))).thenReturn(false);
        when(infoRepository.save(any(OrganizationInfo.class))).thenReturn(info);

        infoService.createOrganizationInfo(infoView);

        verify(infoRepository).save(any(OrganizationInfo.class));
    }

    @Test(expected = NotFoundException.class)
    public void deleteNonExistingInfoShouldThrowException() throws Exception {
        when(infoRepository.exists(ID)).thenReturn(false);

        infoService.deleteOrganizationInfo();
    }

    @Test
    public void deleteShouldDeleteFromRepository() throws Exception {
        when(infoRepository.exists(any(UUID.class))).thenReturn(true);
        when(infoRepository.findOne(any(UUID.class))).thenReturn(info);

        infoService.deleteOrganizationInfo();

        verify(infoRepository).delete(any(UUID.class));
    }

    @Test(expected = NotFoundException.class)
    public void updateNonExistingInfoShouldThrowException() throws Exception {
        when(infoRepository.exists(ID)).thenReturn(false);

        infoService.updateOrganizationInfo(infoView);
    }

    @Test
    public void updateShouldSaveToRepository() throws Exception {
        when(infoRepository.exists(any(UUID.class))).thenReturn(true);
        when(infoRepository.findOne(any(UUID.class))).thenReturn(info);
        when(infoRepository.save(any(OrganizationInfo.class))).thenReturn(info);

        infoService.updateOrganizationInfo(infoView);

        verify(infoRepository).save(any(OrganizationInfo.class));
    }
}
