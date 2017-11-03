package com.cfgglobal.test.web.api;

import com.cfgglobal.test.util.aws.AmazonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
@RequestMapping("/v1/attachment")
public class DownloadController {

    @Autowired
    private AmazonService amazonService;

    @GetMapping("/download")
    public void download(@RequestParam String filename, HttpServletResponse response) {
        response.setHeader("Content-Disposition", "inline; filename=" + filename);
        try {
            File file = amazonService.getFile(filename);
            InputStream in = new FileInputStream(file);
            IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }


    }
}
