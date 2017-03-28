package com.intive.patronage.toz.schedule.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@PropertySource("classpath:schedule.properties")
public class ScheduleParser {

    @Value("${MONDAY}")
    private List<String> MONDAY;
    @Value("${TUESDAY}")
    private List<String> TUESDAY;
    @Value("${WEDNESDAY}")
    private List<String> WEDNESDAY;
    @Value("${THURSDAY}")
    private List<String> THURSDAY;
    @Value("${FRIDAY}")
    private List<String> FRIDAY;
    @Value("${SATURDAY}")
    private List<String> SATURDAY;
    @Value("${SUNDAY}")
    private List<String> SUNDAY;
}
