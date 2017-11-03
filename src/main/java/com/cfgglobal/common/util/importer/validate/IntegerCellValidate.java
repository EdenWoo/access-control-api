package com.cfgglobal.common.util.importer.validate;

public class IntegerCellValidate implements CellValidate {
    @Override
    public boolean validate(Object obj) {
        try {
            Integer.parseInt(obj.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
