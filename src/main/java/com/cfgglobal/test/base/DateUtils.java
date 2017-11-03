package com.cfgglobal.test.base;

import io.vavr.collection.List;
import lombok.experimental.UtilityClass;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static java.time.DayOfWeek.*;

@UtilityClass
public class DateUtils {


    public static final List<DayOfWeek> WEEKENDS = List.of(SATURDAY, SUNDAY);
    public static final List<DayOfWeek> WEEKDAYS = List.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);

    public static ZonedDateTime getZonedDateTime(String zone, String str) {
        ZoneId china = ZoneId.of(zone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.parse(str, formatter), china).withZoneSameInstant(ZoneOffset.UTC);


        return zonedDateTime;
    }

    public LocalDate add(LocalDate date, int workdays) {
        if (workdays < 1) {
            return date;
        }

        LocalDate result = date;
        int addedDays = 0;
        while (addedDays < workdays) {
            result = result.plusDays(1);
            if (!(result.getDayOfWeek() == SATURDAY ||
                    result.getDayOfWeek() == SUNDAY)) {
                ++addedDays;
            }
        }

        return result;
    }

    public ZonedDateTime add(ZonedDateTime date, int workdays) {
        if (workdays < 1) {
            return date;
        }

        ZonedDateTime result = date;
        int addedDays = 0;
        while (addedDays < workdays) {
            result = result.plusDays(1);
            if (!(result.getDayOfWeek() == SATURDAY ||
                    result.getDayOfWeek() == SUNDAY)) {
                ++addedDays;
            }
        }

        return result;
    }

    public boolean isWeekend(LocalDate localDate) {
        return WEEKENDS.contains(localDate.getDayOfWeek());
    }

    public boolean isWeekday(LocalDate localDate) {
        return WEEKDAYS.contains(localDate.getDayOfWeek());
    }
}
