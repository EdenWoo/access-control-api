package com.cfgglobal.common.util.importer.parser;

import com.cfgglobal.common.util.importer.convert.CellConvertor;
import com.cfgglobal.common.util.importer.validate.CellValidate;
import lombok.Data;

@Data
public class CellParser {

    protected int index;

    protected String attribute;

    protected CellConvertor convert;

    protected CellValidate validate;

    static CellParser create(int index, String attribute) {
        CellParser cellParser = new CellParser();
        cellParser.setIndex(index);
        cellParser.setAttribute(attribute);
        return cellParser;
    }

    static CellParser create(int index, String attribute, CellConvertor convert) {
        CellParser cellParser = create(index, attribute);
        cellParser.setConvert(convert);
        return cellParser;
    }


    static CellParser create(int index, String attribute, CellConvertor convert, CellValidate validate) {
        CellParser cellParser = create(index, attribute);
        cellParser.setConvert(convert);
        cellParser.setValidate(validate);
        return cellParser;
    }

}
