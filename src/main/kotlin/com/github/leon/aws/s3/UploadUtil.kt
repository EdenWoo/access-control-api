package com.github.leon.aws.s3

import com.github.leon.ams.s3.AmazonService
import com.github.leon.ams.s3.UploadData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

import java.io.File
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Component
class UploadUtil {
    @Autowired
    private val amazonService: AmazonService? = null

    fun write(file: File, identification: String): String {

        val data = Files.readAllBytes(file.toPath())
        return upload(data, file.name, identification)

    }

    fun write(file: MultipartFile, identification: String): String {

        val data = file.bytes
        return upload(data, file.originalFilename, identification)

    }

    fun write(data: ByteArray, fileName: String, identification: String): String {
        return upload(data, fileName, identification)

    }

    @Throws(Exception::class)
    private fun upload(data: ByteArray, fileName: String, identification: String): String {
        val newName = generateFilename(fileName, identification)
        amazonService!!.upload(UploadData(newName, data))
        return newName
    }

    private fun generateFilename(fileName: String, identification: String): String {
        return LocalDateTime.now(ZoneId.of("Pacific/Auckland")).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")) +
                "_" +
                identification +
                fileName.substring(fileName.lastIndexOf("."))
    }

    companion object {

        private const val DOWNLOAD_URL_PREFIX = "/attachment/download/"

        fun getDownloadUrl(module: String, newFilename: String, preview: Boolean): String {
            return DOWNLOAD_URL_PREFIX + module + "?filename=" + newFilename + if (preview) "&preview=true" else ""
        }
    }

}
