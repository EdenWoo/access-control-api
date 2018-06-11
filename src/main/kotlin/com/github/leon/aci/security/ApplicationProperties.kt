package com.github.leon.aci.security

import org.hibernate.validator.constraints.NotEmpty
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app")
class ApplicationProperties {


    //@NotEmpty
    var rateDataPath: String? = null

    var userClass: String? = null

    @NotEmpty
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

        var enumPackages: List<String> = listOf(
                "com.github.leon.aci.enums"
        )

        var enums: List<String> = listOf()

        var entityScanPackages: List<String> = listOf(
                "com.github.leon.aci.domain",
                "com.github.leon.email.domain",
                "com.github.leon.setting.domain",
                "com.github.leon.sms.domain",
                "com.github.leon.backup.domain",
                "com.github.leon.sysconfig.domain"
        )

        var dtoScanPackages: List<String> = listOf("com.github.leon.aci.web.api.vo")

    }
}
