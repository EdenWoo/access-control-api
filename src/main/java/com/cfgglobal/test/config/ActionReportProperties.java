package com.cfgglobal.test.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mvc.action.report")
@Data
public class ActionReportProperties {
    private boolean isMaven = true;
    private String module;
    private boolean switcher = false;
}