package com.cfgglobal.common.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.experimental.Accessors;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Accessors(fluent = true)
public class PoiExporter {

    public static final String VERSION_2003 = "2003";
    private final static String CONTENT_TYPE = "application/msexcel;charset=UTF-8";
    private static final int HEADER_ROW = 1;
    private static final int MAX_ROWS = 65535;
    private String version;
    private String[] sheetNames = new String[]{"sheet"};
    private int cellWidth = 8000;
    private int headerRow;
    private String[][] headers;
    private String[][] columns;
    private List[] data;
    private String fileName;

    public PoiExporter(List<?>... data) {
        this.data = data;
    }

    public static PoiExporter data(List<?>... data) {
        return new PoiExporter(data);
    }

    public static PoiExporter data(io.vavr.collection.List<?>... data) {
        List[] newData = new List[data.length];
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i].asJava();
        }
        return new PoiExporter(newData);
    }


    public static List<List<?>> dice(List<?> num, int chunkSize) {
        int size = num.size();
        int chunk_num = size / chunkSize + (size % chunkSize == 0 ? 0 : 1);
        List<List<?>> result = Lists.newArrayList();
        for (int i = 0; i < chunk_num; i++) {
            result.add(Lists.newArrayList(num.subList(i * chunkSize, i == chunk_num - 1 ? size : (i + 1) * chunkSize)));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static void processAsMap(String[] columns, Row row, Object obj) {
        Cell cell;
        Map<String, Object> map = (Map<String, Object>) obj;
        // show all if column not specified
        if (columns.length == 0) {
            Set<String> keys = map.keySet();
            int columnIndex = 0;
            for (String key : keys) {
                cell = row.createCell(columnIndex);
                cell.setCellValue(map.get(key) + "");
                columnIndex++;
            }
        } else {
            for (int j = 0, len = columns.length; j < len; j++) {
                cell = row.createCell(j);
                cell.setCellValue(map.get(columns[j]) == null ? "" : map.get(columns[j]) + "");
            }
        }
    }

    public void render(HttpServletResponse response) {

        Workbook workbook = this.export();
        response.setHeader("Content-disposition", "attachment; filename=" + Option.of(fileName).getOrElse(Instant.now().getEpochSecond() + ".xls"));
        response.setContentType(CONTENT_TYPE);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            workbook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public Workbook export() {
        Preconditions.checkNotNull(data, "data can not be null");
        Preconditions.checkNotNull(headers, "headers can not be null");
        Preconditions.checkNotNull(columns, "columns can not be null");
        Preconditions.checkArgument(
                data.length == sheetNames.length
                        && sheetNames.length == headers.length
                        && headers.length == columns.length,
                "data,sheetNames,headers and columns'length should be the same."
                        + "(data:"
                        + data.length
                        + ",sheetNames:"
                        + sheetNames.length
                        + ",headers:"
                        + headers.length
                        + ",columns:"
                        + columns.length
                        + ")");
        Preconditions.checkArgument(cellWidth >= 0, "cellWidth can not be less than 0");
        Workbook wb;
        if (VERSION_2003.equals(version)) {
            wb = new HSSFWorkbook();
            if (data.length > 1) {
                for (int i = 0; i < data.length; i++) {
                    List<?> item = data[i];
                    Preconditions.checkArgument(
                            item.size() < MAX_ROWS,
                            "Data ["
                                    + i
                                    + "] is invalid:invalid data size ("
                                    + item.size()
                                    + ") outside allowable range (0..65535)");
                }
            } else if (data.length == 1 && data[0].size() > MAX_ROWS) {
                data = dice(data[0], MAX_ROWS).toArray(new List<?>[]{});
                String sheetName = sheetNames[0];
                sheetNames = new String[data.length];
                for (int i = 0; i < data.length; i++) {
                    sheetNames[i] = sheetName + (i == 0 ? "" : (i + 1));
                }
                String[] header = headers[0];
                headers = new String[data.length][];
                for (int i = 0; i < data.length; i++) {
                    headers[i] = header;
                }
                String[] column = columns[0];
                columns = new String[data.length][];
                for (int i = 0; i < data.length; i++) {
                    columns[i] = column;
                }
            }
        } else {
            wb = new SXSSFWorkbook(1000);
        }
        if (data.length == 0) {
            return wb;
        }
        for (int i = 0; i < data.length; i++) {
            Sheet sheet = wb.createSheet(sheetNames[i]);
            Row row;
            Cell cell;
            if (headers[i].length > 0) {
                row = sheet.createRow(0);
                if (headerRow <= 0) {
                    headerRow = HEADER_ROW;
                }
                headerRow = Math.min(headerRow, MAX_ROWS);
                for (int h = 0, lenH = headers[i].length; h < lenH; h++) {
                    if (cellWidth > 0) {
                        sheet.setColumnWidth(h, cellWidth);
                    }
                    cell = row.createCell(h);
                    cell.setCellValue(headers[i][h]);
                }
            }

            for (int j = 0, len = data[i].size(); j < len; j++) {
                row = sheet.createRow(j + headerRow);
                Object obj = data[i].get(j);
                if (obj == null) {
                    continue;
                }
                if (obj instanceof Map) {
                    processAsMap(columns[i], row, obj);
                } else {
                    Map map = Try.of(() -> BeanUtils.describe(obj)).get();
                    processAsMap(columns[i], row, map);
                    // throw new RuntimeException("Not support type[" + obj.getClass() + "]");
                }
            }
        }
        return wb;
    }

    public void export(File file) {

    }

    public PoiExporter version(String version) {
        this.version = version;
        return this;
    }

    public PoiExporter sheetName(String sheetName) {
        this.sheetNames = new String[]{sheetName};
        return this;
    }

    public PoiExporter sheetNames(String... sheetName) {
        this.sheetNames = sheetName;
        return this;
    }

    public PoiExporter cellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
        return this;
    }

    public PoiExporter headerRow(int headerRow) {
        this.headerRow = headerRow;
        return this;
    }

    public PoiExporter header(String... header) {
        this.headers = new String[][]{header};
        return this;
    }

    public PoiExporter headers(String[]... headers) {
        this.headers = headers;
        return this;
    }

    public PoiExporter column(String... column) {
        this.columns = new String[][]{column};
        return this;
    }

    public PoiExporter columns(String[]... columns) {
        this.columns = columns;
        return this;
    }

    public PoiExporter fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
}
