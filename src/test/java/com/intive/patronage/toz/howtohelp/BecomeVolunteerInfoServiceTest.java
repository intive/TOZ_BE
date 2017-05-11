package com.intive.patronage.toz.howtohelp;

import com.intive.patronage.toz.error.exception.AlreadyExistsException;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.howtohelp.model.db.HelpInfo;
import com.intive.patronage.toz.howtohelp.model.enumeration.HelpInfoType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.intive.patronage.toz.howtohelp.DonateInfoServiceTest.DESCRIPTION;
import static com.intive.patronage.toz.howtohelp.DonateInfoServiceTest.MODIFICATION_DATE;
import static com.intive.patronage.toz.howtohelp.model.enumeration.HelpInfoType.HOW_TO_BECOME_VOLUNTEER;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BecomeVolunteerInfoServiceTest {


    private final static HelpInfoType INFO_TYPE = HOW_TO_BECOME_VOLUNTEER;

    @Mock
    private HelpInfoRepository helpInfoRepository;
    @InjectMocks
    private BecomeVolunteerInfoService becomeVolunteerInfoService;
    private HelpInfo helpInfo;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        helpInfo = new HelpInfo(INFO_TYPE, DESCRIPTION, MODIFICATION_DATE);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowErrorIfNotExists() throws Exception {
        when(helpInfoRepository.exists(INFO_TYPE)).thenReturn(false);

        becomeVolunteerInfoService.findHelpInfo();
    }

    @Test
    public void findShouldCallRepository() throws Exception {
        when(helpInfoRepository.exists(any(HelpInfoType.class))).thenReturn(true);
        when(helpInfoRepository.findOne(any(HelpInfoType.class))).thenReturn(helpInfo);

        becomeVolunteerInfoService.findHelpInfo();

        verify(helpInfoRepository).findOne(any(HelpInfoType.class));
    }

    @Test(expected = AlreadyExistsException.class)
    public void createExistingInfoShouldThrowException() throws Exception {
        when(helpInfoRepository.exists(any(HelpInfoType.class))).thenReturn(true);

        becomeVolunteerInfoService.createHelpInfo(helpInfo);
    }

    @Test
    public void createShouldSaveToRepository() throws Exception {
        when(helpInfoRepository.exists(any(HelpInfoType.class))).thenReturn(false);
        when(helpInfoRepository.save(any(HelpInfo.class))).thenReturn(helpInfo);

        becomeVolunteerInfoService.createHelpInfo(helpInfo);

        verify(helpInfoRepository).save(any(HelpInfo.class));
    }

    @Test(expected = NotFoundException.class)
    public void updateNonExistingInfoShouldThrowException() throws Exception {
        when(helpInfoRepository.exists(INFO_TYPE)).thenReturn(false);

        becomeVolunteerInfoService.updateHelpInfo(helpInfo);
    }

    @Test
    public void updateShouldSaveToRepository() throws Exception {
        when(helpInfoRepository.exists(any(HelpInfoType.class))).thenReturn(true);
        when(helpInfoRepository.findOne(any(HelpInfoType.class))).thenReturn(helpInfo);
        when(helpInfoRepository.save(any(HelpInfo.class))).thenReturn(helpInfo);
        becomeVolunteerInfoService.updateHelpInfo(helpInfo);

        verify(helpInfoRepository).save(any(HelpInfo.class));
    }
}
