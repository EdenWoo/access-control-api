package com.github.leon.aci.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app")
class ApplicationProperties {


    var domain: String? = null

    var rateDataPath: String? = null

    var userClass: String? = null

    var userCookie: String? = null


    var jwt: Jwt = Jwt()

    var user: User = User()


    class Jwt {
         var header = "Authorization"
         var expiresIn: Long? = 864000L
         var secret = "queenvictoria"
         var cookie = "AUTH-TOKEN"
         var param = "token"
         var anonymousUrls: String? = null
    }

    class User {
        var needVerify  = false
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
