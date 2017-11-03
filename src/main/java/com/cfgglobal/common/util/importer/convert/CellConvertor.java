package com.cfgglobal.common.util.importer.convert;

@FunctionalInterface
public interface CellConvertor<T> {
    T convert(String val, T obj);
}
