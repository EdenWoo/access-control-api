package com.cfgglobal.test.web.api;

import com.cfgglobal.common.util.importer.PoiImporter;
import com.cfgglobal.common.util.importer.parser.FileParser;
import com.cfgglobal.test.service.excel.ExcelParsingRule;
import com.cfgglobal.test.web.base.BaseController;
import io.vavr.collection.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;



@RestController
@RequestMapping("/v1/excel")

@Slf4j
public class ExcelController extends BaseController {

    @Autowired
    java.util.List<ExcelParsingRule> excelParsingRules;

    @PostMapping
    public void submit(MultipartFile file, String rule) throws IOException {
        ExcelParsingRule excelParsingRule = List.ofAll(excelParsingRules).filter(e->e.getRuleName().equals(rule)).head();
        String fileName = "/tmp/" + Instant.now().getEpochSecond();
        File tmpFile = new File(fileName);
        file.transferTo(tmpFile);
        FileParser fileParser = excelParsingRule.getFileParser();
        excelParsingRule.process(PoiImporter.processSheet(tmpFile,fileParser,excelParsingRule.getEntityClass()));
    }
}