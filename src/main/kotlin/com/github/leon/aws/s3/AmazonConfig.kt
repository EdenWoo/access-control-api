package com.github.leon.aws.s3


import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.github.leon.ams.s3.AmazonProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AmazonProperties::class)
class AmazonConfig {

    @Autowired
    private val amazonProperties: AmazonProperties? = null

    @Bean
    fun createAmazonS3Client(): AmazonS3 {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(
                        AWSStaticCredentialsProvider(
                                BasicAWSCredentials(amazonProperties!!.accessKey, amazonProperties.secretKey)
                        )
                ).withRegion(Regions.AP_SOUTHEAST_2).build()
    }
}
