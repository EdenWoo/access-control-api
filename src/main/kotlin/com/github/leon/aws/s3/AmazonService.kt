package com.github.leon.aws.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.*
import com.github.leon.ams.s3.AmazonProperties
import com.github.leon.ams.s3.UploadData
import lombok.SneakyThrows
import lombok.extern.slf4j.Slf4j
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service

import java.io.*

@Slf4j
@Service
@EnableConfigurationProperties(AmazonProperties::class)
class AmazonService {

    @Autowired
    private val aws: AmazonProperties? = null

    @Autowired
    private val s3Client: AmazonS3? = null

    /**
     * @param data to be uploaded
     * @return the unique key start the file in AWS S3
     */
    fun upload(data: UploadData): String {
        val key = aws!!.key + data.name
        val metadata = ObjectMetadata()
        metadata.contentLength = data.data.size.toLong()
        val result = s3Client!!.putObject(PutObjectRequest(aws.bucketName, key, ByteArrayInputStream(data.data), metadata))
        log.info("file uploaded result: {}", result)
        return aws.regionLink + key
    }

    fun download(fileName: String): ByteArray? {
        val key = aws!!.key + fileName
        log.info("Downloading from S3: {}$key")
        val `object` = s3Client!!.getObject(GetObjectRequest(aws.bucketName, key))
        var data: ByteArray? = null
        try {
            `object`.objectContent.use { reader -> data = IOUtils.toByteArray(reader) }
        } catch (e: IOException) {
            log.error("Error in downloading object from S3", e)
        }

        return data
    }

    @SneakyThrows
    fun downloadInputStream(fileName: String): InputStream {
        val key = aws!!.key + fileName
        log.info("Downloading from S3: {}$key")
        val `object` = s3Client!!.getObject(GetObjectRequest(aws.bucketName, key))
        return `object`.objectContent
    }

    @SneakyThrows
    fun getFile(fileName: String): File {
        val key = aws!!.key + fileName
        log.info("Downloading from S3: {}$key")
        val `object` = s3Client!!.getObject(GetObjectRequest(aws.bucketName, key))
        val inputStream = `object`.objectContent


        val targetFile = File("/tmp/$fileName")
        val outStream = FileOutputStream(targetFile)
        IOUtils.copy(inputStream, outStream)
        return targetFile
    }

    fun remove(fileName: String) {
        val key = aws!!.key + fileName
        s3Client!!.deleteObject(DeleteObjectRequest(aws.bucketName, key))
    }

    /**
     * Get the url for Public access.
     * The permission need to set to public on s3 service.
     *
     * @param key
     * @return
     */
    fun getUrl(key: String): String {
        return s3Client!!.getUrl(aws!!.bucketName, key).toString()
    }

}
