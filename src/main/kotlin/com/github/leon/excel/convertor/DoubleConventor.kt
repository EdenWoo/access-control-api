package com.github.leon.excel.convertor

import com.github.leon.files.convert.CellConverter


class DoubleConventor : CellConverter{
    override fun convert(value: String, obj: Any): Any {
        if (value.isEmpty()) {
            return 0.0
        }
        return java.lang.Double.parseDouble(value)
    }
}
