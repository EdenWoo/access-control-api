package com.cfgglobal.common.util.importer.parser;

import java.util.List;

@FunctionalInterface
public interface RowProcessor {

    void exec(Object model, List<String> list, int rowIndex);
}
