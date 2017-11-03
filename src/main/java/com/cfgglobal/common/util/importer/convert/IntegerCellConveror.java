package com.cfgglobal.common.util.importer.convert;

public class IntegerCellConveror implements CellConvertor {
    @Override
    public Object convert(String val, Object obj) {
        return Integer.parseInt(val);
    }
}
