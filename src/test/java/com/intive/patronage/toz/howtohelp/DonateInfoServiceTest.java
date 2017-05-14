package com.intive.patronage.toz.howtohelp;

import com.intive.patronage.toz.error.exception.AlreadyExistsException;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.howtohelp.model.db.HelpInfo;
import com.intive.patronage.toz.howtohelp.model.enumeration.HelpInfoType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Date;

import static com.intive.patronage.toz.howtohelp.BecomeVolunteerInfoServiceTest.NOT_SHORTENED;
import static com.intive.patronage.toz.howtohelp.model.enumeration.HelpInfoType.HOW_TO_DONATE;
import static com.intive.patronage.toz.util.StringFormatter.trimToLengthPreserveWord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DonateInfoServiceTest {

    private static final HelpInfoType INFO_TYPE = HOW_TO_DONATE;
    static final  Date MODIFICATION_DATE = Date.from(Instant.now());
    static final String DESCRIPTION = RandomStringUtils.random(200, "abcd ");
    private static final int SHORTENED_INFO_LENGTH = 100;
    static final String DESCRIPTION_SHORTENED = trimToLengthPreserveWord(DESCRIPTION, SHORTENED_INFO_LENGTH);
    static final Boolean SHORTENED = true;

    @Mock
    private HelpInfoRepository helpInfoRepository;
    @InjectMocks
    private DonateInfoService donateInfoService;
    private static final HelpInfo helpInfo = new HelpInfo(INFO_TYPE, DESCRIPTION, MODIFICATION_DATE);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findShouldGetShortenedInfo() {
        when(helpInfoRepository.exists(any(HelpInfoType.class))).thenReturn(true);
        when(helpInfoRepository.findOne(any(HelpInfoType.class))).thenReturn(helpInfo);

        HelpInfo shortenedInfo = donateInfoService.findHelpInfo(SHORTENED);

        assertThat(shortenedInfo.getHowToHelpDescription()).isEqualTo(DESCRIPTION_SHORTENED);
        verify(helpInfoRepository).findOne(any(HelpInfoType.class));
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowErrorIfNotExists() throws Exception {
        when(helpInfoRepository.exists(INFO_TYPE)).thenReturn(false);

        donateInfoService.findHelpInfo(NOT_SHORTENED);
    }

    @Test
    public void findShouldCallRepository() throws Exception {
        when(helpInfoRepository.exists(any(HelpInfoType.class))).thenReturn(true);
        when(helpInfoRepository.findOne(any(HelpInfoType.class))).thenReturn(helpInfo);

        HelpInfo getHelpInfo = donateInfoService.findHelpInfo(NOT_SHORTENED);

        assertThat(getHelpInfo).isEqualToComparingFieldByField(helpInfo);
        verify(helpInfoRepository).findOne(any(HelpInfoType.class));
    }

    @Test(expected = AlreadyExistsException.class)
    public void createExistingInfoShouldThrowException() throws Exception {
        when(helpInfoRepository.exists(any(HelpInfoType.class))).thenReturn(true);

        donateInfoService.createHelpInfo(helpInfo);
    }

    @Test
    public void createShouldSaveToRepository() throws Exception {
        when(helpInfoRepository.exists(any(HelpInfoType.class))).thenReturn(false);
        when(helpInfoRepository.save(any(HelpInfo.class))).thenReturn(helpInfo);

        HelpInfo createdInfo = donateInfoService.createHelpInfo(helpInfo);

        assertThat(createdInfo).isEqualToComparingFieldByField(helpInfo);
        verify(helpInfoRepository).save(any(HelpInfo.class));
    }

    @Test(expected = NotFoundException.class)
    public void updateNonExistingInfoShouldThrowException() throws Exception {
        when(helpInfoRepository.exists(INFO_TYPE)).thenReturn(false);

        donateInfoService.updateHelpInfo(helpInfo);
    }

    @Test
    public void updateShouldSaveToRepository() throws Exception {
        when(helpInfoRepository.exists(any(HelpInfoType.class))).thenReturn(true);
        when(helpInfoRepository.findOne(any(HelpInfoType.class))).thenReturn(helpInfo);
        when(helpInfoRepository.save(any(HelpInfo.class))).thenReturn(helpInfo);
        HelpInfo updatedInfo = donateInfoService.updateHelpInfo(helpInfo);

        assertThat(updatedInfo).isEqualToComparingFieldByField(helpInfo);
        verify(helpInfoRepository).save(any(HelpInfo.class));
    }
}
