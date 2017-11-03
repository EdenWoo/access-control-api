package com.cfgglobal.test.config.aws;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AmazonProperties.class)
public class AmazonConfig {

    @Autowired
    private AmazonProperties amazonProperties;

    @Bean
    public AmazonS3 createAmazonS3Client() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(amazonProperties.getAccessKey(), amazonProperties.getSecretKey())
                        )
                ).withRegion(Regions.AP_SOUTHEAST_2).build();
    }
}
