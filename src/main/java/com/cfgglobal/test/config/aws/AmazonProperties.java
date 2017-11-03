package com.cfgglobal.test.config.aws;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("aws.s3")
public class AmazonProperties {

    private String bucketName;
    private String accessKey;
    private String secretKey;
    private String key;
    private String regionLink;
}
