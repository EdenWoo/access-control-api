package com.cfgglobal.common.util.importer.filter;

import java.util.List;

@FunctionalInterface
public interface RowFilter {
    boolean doFilter(int rowNum, List<String> list);
}
