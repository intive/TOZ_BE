package com.intive.patronage.toz.schedule;

import com.intive.patronage.toz.Application;
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
import java.util.List;

import static com.intive.patronage.toz.schedule.constant.DateTimeConsts.HOURS_24_REGEX;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
public class ScheduleParserTest extends AbstractJUnit4SpringContextTests {

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
        for (DayConfigView config : dayConfigs){
            assertThat(config).isNotNull();
            for (PeriodView period : config.getPeriods()){
                assertThat(period).isNotNull();
                assertThat(period.getPeriodStart()).matches(HOURS_24_REGEX);
                assertThat(period.getPeriodEnd()).matches(HOURS_24_REGEX);
            }
        }
    }
}
