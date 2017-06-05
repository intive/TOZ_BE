package com.intive.patronage.toz.status;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.status.model.PetStatus;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(DataProviderRunner.class)
public class PetStatusServiceTest {
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Reksio";
    private static final String EXPECTED_RGB = "#7cedaa2";

    @Mock
    private PetsStatusRepository petsStatusRepository;

    @Mock
    private PetsStatusService petsStatusService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        petsStatusService = new PetsStatusService(petsStatusRepository);
    }

    @DataProvider
    public static Object[] getProperPetsStatus() {
        final PetStatus petStatus = new PetStatus();
        petStatus.setId(EXPECTED_ID);
        petStatus.setName(EXPECTED_NAME);
        petStatus.setRgb(EXPECTED_RGB);
        return new PetStatus[]{petStatus};
    }

    @Test
    public void shouldReturnPetsStatusList() throws Exception {
        when(petsStatusRepository.findAll()).thenReturn(Collections.emptyList());

        final List<PetStatus> petStatuses = petsStatusService.findAll();
        assertTrue(petStatuses.isEmpty());
    }


    @Test
    @UseDataProvider("getProperPetsStatus")
    public void shouldCreatePetsStatus(final PetStatus petStatus) throws Exception {
        when(petsStatusRepository.save(any(PetStatus.class))).thenReturn(petStatus);

        final PetStatus createdPetStatus = petsStatusService.create(petStatus);
        assertEquals(EXPECTED_NAME, createdPetStatus.getName());
        assertEquals(EXPECTED_RGB, createdPetStatus.getRgb());
        verify(petsStatusRepository, times(1)).save(any(PetStatus.class));
    }

    @Test
    public void shouldDeletePetsStatus() throws Exception {
        when(petsStatusRepository.exists(EXPECTED_ID)).thenReturn(true);
        petsStatusService.delete(EXPECTED_ID);

        verify(petsStatusRepository, times(1)).exists(eq(EXPECTED_ID));
    }

    @Test(expected = NotFoundException.class)
    public void deletePetsStatusNotFoundException() throws Exception {
        when(petsStatusRepository.exists(EXPECTED_ID)).thenReturn(false);
        petsStatusService.delete(EXPECTED_ID);

        verify(petsStatusRepository, times(1)).exists(eq(EXPECTED_ID));
    }

    @Test
    @UseDataProvider("getProperPetsStatus")
    public void updatePetsStatus(final PetStatus petStatus) throws Exception {
        when(petsStatusRepository.exists(EXPECTED_ID)).thenReturn(true);
        when(petsStatusRepository.save(any(PetStatus.class))).thenReturn(petStatus);
        final PetStatus updatedPetStatus = petsStatusService.update(EXPECTED_ID, petStatus);

        assertEquals(EXPECTED_NAME, updatedPetStatus.getName());
        assertEquals(EXPECTED_RGB, updatedPetStatus.getRgb());
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("getProperPetsStatus")
    public void updateProposalNotFoundException(final PetStatus petStatus) throws Exception {
        when(petsStatusRepository.exists(EXPECTED_ID)).thenReturn(false);
        petsStatusService.update(EXPECTED_ID, petStatus);

        verify(petsStatusRepository, times(1)).exists(eq(EXPECTED_ID));
    }
}
