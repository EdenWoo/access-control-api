package com.cfgglobal.test.util.aws;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class UploadUtil {

    private static final String DOWNLOAD_URL_PREFIX = "/attachment/download/";
    @Autowired
    private AmazonService amazonService;

    public static String getDownloadUrl(@NonNull String module, @NonNull String newFilename, boolean preview) {
        return DOWNLOAD_URL_PREFIX + module + "?filename=" + newFilename + ((preview) ? "&preview=true" : "");
    }

    @SneakyThrows
    public String write(File file, String identification) {

        byte[] data = Files.readAllBytes(file.toPath());
        return upload(data, file.getName(), identification);

    }

    @SneakyThrows
    public String write(MultipartFile file, String identification) {

        byte[] data = file.getBytes();
        return upload(data, file.getOriginalFilename(), identification);

    }

    @SneakyThrows
    public String write(byte[] data, String fileName, String identification) {
        return upload(data, fileName, identification);

    }

    private String upload(@NonNull byte[] data, @NonNull String fileName, @NonNull String identification) throws Exception {
        String newName = generateFilename(fileName, identification);
        amazonService.upload(new UploadData(newName, data));
        return newName;
    }

    private String generateFilename(@NonNull String fileName, @NonNull String identification) {
        return LocalDateTime.now(ZoneId.of("Pacific/Auckland")).format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss")) +
                "_" +
                identification +
                fileName.substring(fileName.lastIndexOf("."));
    }

}
