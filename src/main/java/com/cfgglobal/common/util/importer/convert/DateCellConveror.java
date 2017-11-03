package com.cfgglobal.common.util.importer.convert;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCellConveror implements CellConvertor {
    @Override
    public Object convert(String val, Object obj) {
        Date date = null;
        try {
            if (StringUtils.isNoneBlank(val) && !val.equals("NULL")) {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(val);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
