package com.github.leon.sms.clicksend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("sms.clicksend")
data class ClicksendProperties(

        var apiUrl: String = "",
        var apiKey: String = "",
        var username: String = ""
)