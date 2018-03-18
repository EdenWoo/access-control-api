package com.github.leon.aci.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app")
class ApplicationProperties {


    var domain: String? = null

    var rateDataPath: String? = null

    var userClass: String? = null

    var userCookie: String? = null


    var jwt: Jwt? = null


    class Jwt {
        internal var header = "Authorization"
        internal var expiresIn: Long? = 864000L
        internal var secret = "queenvictoria"
        internal var cookie = "AUTH-TOKEN"
        internal var param = "token"
        internal var anonymousUrls: String? = null
    }

    companion object {

        var myUserClass: String? = null

        var enumPackages: List<String> = listOf()

        var enums: List<String> = listOf()

        var entityScanPackage: List<String> = listOf()

        var dateType = "ZonedDateTime"

        var displayTimeZone = "Pacific/Auckland"

        var dbTimeZone = "UTC"
    }
}
