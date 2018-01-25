package com.cfgglobal.test.service.excel;

import com.cfgglobal.common.util.importer.parser.FileParser;
import com.cfgglobal.test.domain.User;
import com.cfgglobal.test.service.excel.ExcelParsingRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
public class UserExcelParsingRule implements ExcelParsingRule<User> {
    @Override
    public FileParser getFileParser() {
        FileParser fileParser = new FileParser();
        fileParser.setStart(1);
        fileParser.addCell(0, "username");
        fileParser.addCell(1, "password");
        return fileParser;
    }

    @Override
    public Class getEntityClass() {
        return User.class;
    }

    @Override
    public String getRuleName() {
        return "user";
    }

    @Override
    public void process(List<User> data) {
        log.debug("data: {}",data);
    }
}
