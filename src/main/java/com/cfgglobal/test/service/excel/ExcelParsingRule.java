package com.cfgglobal.test.service.excel;

import com.cfgglobal.common.util.importer.parser.FileParser;

import java.util.List;

public interface ExcelParsingRule<T> {


    FileParser getFileParser();

    Class getEntityClass();

    String getRuleName();

    void process(List<T> data);
}
