package com.cfgglobal.test.util.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.cfgglobal.test.config.aws.AmazonProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
@EnableConfigurationProperties(AmazonProperties.class)
public class AmazonService {

    @Autowired
    private AmazonProperties aws;

    @Autowired
    private AmazonS3 s3Client;

    /**
     * @param data to be uploaded
     * @return the unique key start the file in AWS S3
     */
    public String upload(UploadData data) {
        String key = aws.getKey() + data.getName();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(data.getData().length);
        PutObjectResult result = s3Client.putObject(new PutObjectRequest(aws.getBucketName(), key, new ByteArrayInputStream(data.getData()), metadata));
        log.info("file uploaded result: {}", result);
        return aws.getRegionLink() + key;
    }

    public byte[] download(String fileName) {
        String key = aws.getKey() + fileName;
        log.info("Downloading from S3: {}" + key);
        S3Object object = s3Client.getObject(new GetObjectRequest(aws.getBucketName(), key));
        byte[] data = null;
        try (InputStream reader = object.getObjectContent()) {
            data = IOUtils.toByteArray(reader);
        } catch (IOException e) {
            log.error("Error in downloading object from S3", e);
        }
        return data;
    }

    @SneakyThrows
    public InputStream downloadInputStream(String fileName) {
        String key = aws.getKey() + fileName;
        log.info("Downloading from S3: {}" + key);
        S3Object object = s3Client.getObject(new GetObjectRequest(aws.getBucketName(), key));
        return object.getObjectContent();
    }

    @SneakyThrows
    public File getFile(String fileName) {
        String key = aws.getKey() + fileName;
        log.info("Downloading from S3: {}" + key);
        S3Object object = s3Client.getObject(new GetObjectRequest(aws.getBucketName(), key));
        InputStream inputStream = object.getObjectContent();


        File targetFile = new File("/tmp/" + fileName);
        OutputStream outStream = new FileOutputStream(targetFile);
        IOUtils.copy(inputStream, outStream);
        return targetFile;
    }

    public void remove(String fileName) {
        String key = aws.getKey() + fileName;
        s3Client.deleteObject(new DeleteObjectRequest(aws.getBucketName(), key));
    }

    /**
     * Get the url for Public access.
     * The permission need to set to public on s3 service.
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        return s3Client.getUrl(aws.getBucketName(), key).toString();
    }

}
