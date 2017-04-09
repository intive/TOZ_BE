package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.Application;
import com.intive.patronage.toz.schedule.constant.DateTimePattern;
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
import java.util.regex.Pattern;

import static com.intive.patronage.toz.schedule.util.DateUtil.convertToDate;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
public class ScheduleParserTest extends AbstractJUnit4SpringContextTests {

    private static final String DASH = "-";
    private final Pattern CONFIG_HOURS = Pattern.compile(
            String.format("%s-%s", DateTimePattern.localTime24(), DateTimePattern.localTime24()));

    @Autowired
    private ScheduleParser scheduleParser;

    @Test
    public void scheduleInConfigShouldBeProperlyFormatted() {
        for (DayOfWeek day : DayOfWeek.values()) {
            for (String hours : scheduleParser.getSchedule().get(day)) {
                assertThat(CONFIG_HOURS.matcher(hours).matches()).isTrue();
            }
        }
    }

    @Test
    public void parserShouldReturnValidDayConfig() {
        List<DayConfigView> dayConfigs = scheduleParser.getDaysConfig();
        assertThat(dayConfigs).extracting(DayConfigView::getPeriods).isNotNull();
        assertThat(dayConfigs).flatExtracting(DayConfigView::getPeriods)
                .flatExtracting(PeriodView::getPeriodStart, PeriodView::getPeriodEnd)
                .allMatch(hour -> DateTimePattern.localTime24().matcher(hour.toString()).matches());
    }

    @Test
    public void parserShouldApproveCorrectHours() {
        String[] mondayHours = getFirstHoursOnMonday().split(DASH);
        Date from = convertToDate(getMondayDate(), mondayHours[0], ZoneOffset.UTC);
        Date to = convertToDate(getMondayDate(), mondayHours[1], ZoneOffset.UTC);
        scheduleParser.validateHours(from, to);
    }

    @Test(expected = InvalidReservationHoursException.class)
    public void parserShouldDisapproveInvalidHours() {
        String[] mondayHours = getFirstHoursOnMonday().split(DASH);
        String invalidHour = LocalTime.parse(mondayHours[0]).plusMinutes(1).toString();
        Date from = convertToDate(getMondayDate(), invalidHour, ZoneOffset.UTC);
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
