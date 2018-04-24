package com.github.leon.aws.s3


import org.hibernate.validator.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.validation.constraints.NotNull

@ConfigurationProperties("aws.s3")
data class AmazonProperties(
        @NotBlank
        var bucketName: String? = null,

        @NotBlank
        var accessKey: String? = null,

        @NotBlank
        var secretKey: String? = null,

        @NotBlank
        var key: String? = null,
        @NotBlank
        var regionLink: String? = null
)
