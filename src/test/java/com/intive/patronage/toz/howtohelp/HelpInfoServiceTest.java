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

import java.time.Instant;
import java.util.Date;

import static com.intive.patronage.toz.howtohelp.model.enumeration.HelpInfoType.HOW_TO_DONATE;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HelpInfoServiceTest {

    private final static HelpInfoType INFO_TYPE = HOW_TO_DONATE;
    private final static Date MODIFICATION_DATE = Date.from(Instant.now());
    private final static String DESCRIPTION = "string";

    @Mock
    private HelpInfoRepository helpInfoRepository;
    @InjectMocks
    private HelpInfoService helpInfoService;
    private HelpInfo helpInfo;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        helpInfo = new HelpInfo(INFO_TYPE, DESCRIPTION, MODIFICATION_DATE);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowErrorIfNotExists() throws Exception {
        when(helpInfoRepository.exists(INFO_TYPE)).thenReturn(false);

        helpInfoService.findHowToHelpInfo(INFO_TYPE);
    }

    @Test
    public void findShouldCallRepository() throws Exception {
        when(helpInfoRepository.exists(any(HelpInfoType.class))).thenReturn(true);
        when(helpInfoRepository.findOne(any(HelpInfoType.class))).thenReturn(helpInfo);

        helpInfoService.findHowToHelpInfo(INFO_TYPE);

        verify(helpInfoRepository).findOne(any(HelpInfoType.class));
    }

    @Test(expected = AlreadyExistsException.class)
    public void createExistingInfoShouldThrowException() throws Exception {
        when(helpInfoRepository.exists(any(HelpInfoType.class))).thenReturn(true);

        helpInfoService.createHowToHelpInfo(helpInfo, INFO_TYPE);
    }

    @Test
    public void createShouldSaveToRepository() throws Exception {
        when(helpInfoRepository.exists(any(HelpInfoType.class))).thenReturn(false);
        when(helpInfoRepository.save(any(HelpInfo.class))).thenReturn(helpInfo);

        helpInfoService.createHowToHelpInfo(helpInfo, INFO_TYPE);

        verify(helpInfoRepository).save(any(HelpInfo.class));
    }

    @Test(expected = NotFoundException.class)
    public void updateNonExistingInfoShouldThrowException() throws Exception {
        when(helpInfoRepository.exists(INFO_TYPE)).thenReturn(false);

        helpInfoService.updateHowToHelpInfo(helpInfo, INFO_TYPE);
    }

    @Test
    public void updateShouldSaveToRepository() throws Exception {
        when(helpInfoRepository.exists(any(HelpInfoType.class))).thenReturn(true);
        when(helpInfoRepository.findOne(any(HelpInfoType.class))).thenReturn(helpInfo);
        when(helpInfoRepository.save(any(HelpInfo.class))).thenReturn(helpInfo);
        helpInfoService.updateHowToHelpInfo(helpInfo, INFO_TYPE);

        verify(helpInfoRepository).save(any(HelpInfo.class));
    }
}
