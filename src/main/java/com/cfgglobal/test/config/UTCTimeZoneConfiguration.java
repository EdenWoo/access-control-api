package com.cfgglobal.test.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.TimeZone;

@Configuration
public class UTCTimeZoneConfiguration implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {

        System.setProperty("user.timezone", "Pacific/Auckland");
        //TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        TimeZone.setDefault(TimeZone.getTimeZone("Pacific/Auckland"));
    }

    public void contextDestroyed(ServletContextEvent event) {
    }
}