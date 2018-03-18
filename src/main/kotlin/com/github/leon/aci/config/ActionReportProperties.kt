package com.github.leon.aci.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("mvc.action.report")
class ActionReportProperties {
    var isMaven = true

    var module: String? = null

    var isSwitcher = false

    var isVisitRecord = false

    var isFirewall = false

}