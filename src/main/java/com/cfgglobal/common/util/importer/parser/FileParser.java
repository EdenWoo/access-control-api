package com.cfgglobal.common.util.importer.parser;

import com.cfgglobal.common.util.importer.convert.CellConvertor;
import com.cfgglobal.common.util.importer.validate.CellValidate;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class FileParser {

    protected String name;

    protected int sheetNo;

    protected int start;

    protected int end;

    protected String rowFilter;

    protected String preExcelProcessor;

    protected String postExcelProcessor;

    protected RowProcessor preRowProcessor;

    protected RowProcessor postRowProcessor;

    protected List<CellParser> cellParsers = Lists.newArrayList();

    public FileParser addCell(CellParser cellParser) {
        cellParsers.add(cellParser);
        return this;
    }

    public FileParser addCell(int index, String attribute) {
        cellParsers.add(CellParser.create(index, attribute));
        return this;
    }

    public FileParser addCell(int index, String attribute, CellConvertor cellConvertor) {
        cellParsers.add(CellParser.create(index, attribute, cellConvertor));
        return this;
    }

    public FileParser addCell(int index, String attribute, CellConvertor convert, CellValidate validate) {
        cellParsers.add(CellParser.create(index, attribute, convert, validate));
        return this;
    }

}
