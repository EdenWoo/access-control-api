package com.github.leon.sms.clicktale.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("sms.clicktale")
data class ClicktaleProperties(
        var apiUrl: String = "",
        var apiKey: String = ""
)