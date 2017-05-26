package com.intive.patronage.toz.status;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.status.model.PetsStatus;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(DataProviderRunner.class)
public class PetsStatusServiceTest {
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
        final PetsStatus petsStatus = new PetsStatus();
        petsStatus.setId(EXPECTED_ID);
        petsStatus.setName(EXPECTED_NAME);
        petsStatus.setRgb(EXPECTED_RGB);
        return new PetsStatus[]{petsStatus};
    }

    @Test
    public void shouldReturnPetsStatusList() throws Exception {
        when(petsStatusRepository.findAll()).thenReturn(Collections.emptyList());

        final List<PetsStatus> petsStatuses = petsStatusService.findAll();
        assertTrue(petsStatuses.isEmpty());
    }


    @Test
    @UseDataProvider("getProperPetsStatus")
    public void shouldCreatePetsStatus(final PetsStatus petsStatus) throws Exception {
        when(petsStatusRepository.save(any(PetsStatus.class))).thenReturn(petsStatus);

        final PetsStatus createdPetsStatus = petsStatusService.create(petsStatus);
        assertEquals(EXPECTED_NAME, createdPetsStatus.getName());
        assertEquals(EXPECTED_RGB, createdPetsStatus.getRgb());
        verify(petsStatusRepository, times(1)).save(any(PetsStatus.class));
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
    public void updatePetsStatus(final PetsStatus petsStatus) throws Exception {
        when(petsStatusRepository.exists(EXPECTED_ID)).thenReturn(true);
        when(petsStatusRepository.save(any(PetsStatus.class))).thenReturn(petsStatus);
        final PetsStatus updatedPetsStatus = petsStatusService.update(EXPECTED_ID, petsStatus);

        assertEquals(EXPECTED_NAME, updatedPetsStatus.getName());
        assertEquals(EXPECTED_RGB, updatedPetsStatus.getRgb());
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("getProperPetsStatus")
    public void updateProposalNotFoundException(final PetsStatus petsStatus) throws Exception {
        when(petsStatusRepository.exists(EXPECTED_ID)).thenReturn(false);
        petsStatusService.update(EXPECTED_ID, petsStatus);

        verify(petsStatusRepository, times(1)).exists(eq(EXPECTED_ID));
    }
}
