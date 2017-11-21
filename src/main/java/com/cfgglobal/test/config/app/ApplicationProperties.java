package com.cfgglobal.test.config.app;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("app")
public class ApplicationProperties {

    public static String myUserClass;

    public static String enumPackage;

    public static List<String> enums;

    public static String[] entityScanPackage;


    String domain;

    String rateDataPath;

    String userClass;

    String userCookie;


    Jwt jwt;

    DateSettings dateSettings;

    @Data
    public static class Jwt {
        String header = "Authorization";
        Long expiresIn = 864000L;
        String secret = "queenvictoria";
        String cookie = "AUTH-TOKEN";
        String param = "token";
        String anonymousUrls;
    }

    @Data
    public static class DateSettings {
        String dateType = "ZonedDateTime";
    }
}
