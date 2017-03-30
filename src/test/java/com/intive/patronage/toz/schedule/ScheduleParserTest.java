package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.Application;
import com.intive.patronage.toz.schedule.excception.InvalidReservationHoursException;
import com.intive.patronage.toz.schedule.model.view.DayConfigView;
import com.intive.patronage.toz.schedule.model.view.PeriodView;
import com.intive.patronage.toz.schedule.util.ScheduleParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static com.intive.patronage.toz.schedule.constant.DateTimeConsts.HOURS_24_REGEX;
import static com.intive.patronage.toz.schedule.util.DateUtil.convertToDate;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
public class ScheduleParserTest extends AbstractJUnit4SpringContextTests {

    private static final String DASH = "-";

    @Autowired
    private ScheduleParser scheduleParser;

    @Test
    public void scheduleInConfigShouldBeProperlyFormatted() {
        for (DayOfWeek day : DayOfWeek.values()) {
            for (String hours : scheduleParser.getSchedule().get(day)) {
                assertThat(hours.matches(String.format(
                        "%s-%s", HOURS_24_REGEX, HOURS_24_REGEX))).isTrue();
            }
        }
    }

    @Test
    public void parserShouldReturnValidDayConfig() {
        List<DayConfigView> dayConfigs = scheduleParser.getDaysConfig();
        assertThat(dayConfigs).isNotNull();
        for (DayConfigView config : dayConfigs) {
            assertThat(config).isNotNull();
            for (PeriodView period : config.getPeriods()) {
                assertThat(period).isNotNull();
                assertThat(period.getPeriodStart()).matches(HOURS_24_REGEX);
                assertThat(period.getPeriodEnd()).matches(HOURS_24_REGEX);
            }
        }
    }

    @Test
    public void parserShouldApproveCorrectHours() {
        String[] mondayHours = getFirstHoursOnMonday().split(DASH);
        Date from = convertToDate(getMondayDate(), mondayHours[0],ZoneOffset.UTC);
        Date to = convertToDate(getMondayDate(), mondayHours[1], ZoneOffset.UTC);
        scheduleParser.validateHours(from, to);
    }

    @Test(expected = InvalidReservationHoursException.class)
    public void parserShouldDisapproveCorrectHours() {
        String[] mondayHours = getFirstHoursOnMonday().split(DASH);
        String invalidHour = LocalTime.parse(mondayHours[0]).plusMinutes(1).toString();
        Date from = convertToDate(getMondayDate(), invalidHour,ZoneOffset.UTC);
        Date to = convertToDate(getMondayDate(), mondayHours[1], ZoneOffset.UTC);
        scheduleParser.validateHours(from, to);
    }

    private String getFirstHoursOnMonday() {
        return scheduleParser.getSchedule().get(DayOfWeek.MONDAY)[0];
    }

    private String getMondayDate() {
        LocalDate localDate = LocalDate.now();
        while (!localDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            localDate = localDate.plusDays(1);
        }
        return localDate.toString();
    }
}
