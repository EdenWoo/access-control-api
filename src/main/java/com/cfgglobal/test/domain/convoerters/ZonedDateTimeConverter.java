package com.cfgglobal.test.domain.convoerters;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

public class ZonedDateTimeConverter implements Converter<String, ZonedDateTime> {
    final ZoneId zoneId;

    public ZonedDateTimeConverter(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public ZonedDateTime convert(String source) {
        Locale locale = LocaleContextHolder.getLocale();
        System.out.println(locale);
        long startTime = Long.parseLong(source);
        return Instant.ofEpochSecond(startTime).atZone(zoneId);
    }
}