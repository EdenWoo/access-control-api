package com.github.leon.excel.convertor

import com.github.leon.files.convert.CellConverter


class LongConventor : CellConverter{
    override fun convert(value: String, obj: Any): Any {
        if (value.isEmpty()) {
            return 0L
        }
        return value.toLong()
    }
}
