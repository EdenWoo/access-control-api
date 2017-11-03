package com.cfgglobal.common.util.importer;

import com.cfgglobal.common.util.importer.convert.CellConvertor;
import com.cfgglobal.common.util.importer.filter.RowFilter;
import com.cfgglobal.common.util.importer.parser.CellParser;
import com.cfgglobal.common.util.importer.parser.FileParser;
import com.cfgglobal.common.util.importer.validate.CellValidate;
import com.google.common.collect.Lists;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.joor.Reflect;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@UtilityClass
@Slf4j
public class PoiImporter {

    public List<List<List<String>>> loadFile(File file, FileParser fileParser) {
        int start = fileParser.getStart();
        int end = fileParser.getEnd();
        List<List<List<String>>> result = Lists.newArrayList();

        if (file.getName().endsWith("csv")) {
            CSVParser csvFileParser = Try.of(() -> CSVFormat.DEFAULT.parse(new FileReader(file))).get();
            List<CSVRecord> csvRecords = Try.of(csvFileParser::getRecords).get();
            List<List<String>> sheetList = Lists.newArrayList();
            int rows = (int) csvFileParser.getRecordNumber();
            if (start <= 0) {
                start = 0;
            }
            if (end >= rows) {
                end = rows;
            } else if (end <= 0) {
                end = rows + end;
            }

            for (int rowIndex = start; rowIndex < end; rowIndex++) {
                CSVRecord record = csvRecords.get(rowIndex);
                List<String> columns = Lists.newArrayList();
                for (int i = 0; i < record.size(); i++) {
                    columns.add(record.get(i));

                }
                sheetList.add(columns);
            }
            result.add(sheetList);

        } else {
            Workbook wb = null;
            try {
                wb = WorkbookFactory.create(file);
            } catch (IOException | InvalidFormatException e) {
                e.printStackTrace();
            }


            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                if (fileParser.getSheetNo() > 0 && i != fileParser.getSheetNo()) {
                    result.add(Lists.newArrayList());
                    continue;
                }
                Sheet sheet = wb.getSheetAt(i);
                List<List<String>> sheetList = Lists.newArrayList();
                int rows = sheet.getLastRowNum();
                if (start <= sheet.getFirstRowNum()) {
                    start = sheet.getFirstRowNum();
                }
                if (end >= rows) {
                    end = rows;
                } else if (end <= 0) {
                    end = rows + end;
                }
                for (int rowIndex = start; rowIndex <= end; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    List<String> columns = Lists.newArrayList();
                    if (row == null)
                        continue;
                    int cellNum = row.getLastCellNum();
                    for (int cellIndex = row.getFirstCellNum(); cellIndex < cellNum; cellIndex++) {

                        Cell cell = row.getCell(cellIndex);
                        if (cell == null) {
                            continue;
                        }
                        int cellType = cell.getCellType();
                        String column = "";
                        switch (cellType) {
                            case Cell.CELL_TYPE_NUMERIC:
//                            DecimalFormat format = new DecimalFormat();
//                            format.setGroupingUsed(false);
                                column = String.valueOf(cell.getNumericCellValue());
                                break;
                            case Cell.CELL_TYPE_STRING:
                                column = cell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                column = cell.getBooleanCellValue() + "";
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                column = cell.getCellFormula();
                                break;
                            case Cell.CELL_TYPE_ERROR:

                            case Cell.CELL_TYPE_BLANK:
                                column = " ";
                                break;
                            default:
                        }
                        columns.add(column.trim());
                    }

                    List<Boolean> rowFilterFlagList = Lists.newArrayList();
                    List<RowFilter> rowFilterList = Lists.newArrayList();
                    for (int k = 0; k < rowFilterList.size(); k++) {
                        RowFilter rowFilter = rowFilterList.get(k);
                        rowFilterFlagList.add(rowFilter.doFilter(rowIndex, columns));
                    }
                    if (!rowFilterFlagList.contains(false)) {
                        sheetList.add(columns);
                    }
                }
                result.add(sheetList);
            }

        }
        return result;

    }

    public List<List<String>> loadSheet(File file, FileParser fileParser) {
        return loadFile(file, fileParser).get(fileParser.getSheetNo());
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> List<T> processSheet(File file, FileParser fileParser, Class clazz) {
        List<List<String>> srcList = loadSheet(file, fileParser);
        log.debug(" result  {}", srcList);
        List<T> results = Lists.newArrayList();
        for (int i = 0; i < srcList.size(); i++) {
            List<String> list = srcList.get(i);
            results.add((T) fillModel(clazz, list, fileParser, i));
        }
        return results;
    }


    public Object fillModel(Class<?> clazz, List<String> list, FileParser fileParser, int rowIndex) {
        Object model = Reflect.on(clazz).create().get();
        Option.of(fileParser.getPreRowProcessor()).forEach(e -> e.exec(model, list, rowIndex));

        String message = "";
        for (int i = 0; i < list.size(); i++) {
            String value = list.get(i);
            CellParser cell = matchCell(fileParser, i);
            if (cell == null) {
                continue;
            }
            String name = cell.getAttribute();
            CellValidate cellValidate = cell.getValidate();
            boolean valid = true;
            if (cellValidate != null) {
                valid = cellValidate.validate(value);
                if (!valid) {
                    message = message + "value(" + value + ") is invalid in row " + (rowIndex + 1) + " column " + cell.getIndex() + "\n";
                }
            }
            if (valid) {
                Object convertedValue = value;
                CellConvertor cellConvertor = cell.getConvert();
                if (cellConvertor != null) {
                    convertedValue = cellConvertor.convert(value, model);
                }
                final Object obj = convertedValue;
                Option.of(name).map(e -> e.split(","))
                        .map(io.vavr.collection.List::of)
                        .get()
                        .forEach(e -> Reflect.on(model).set(e, obj));
            }
        }
        Option.of(fileParser.getPostRowProcessor()).forEach(e -> e.exec(model, list, rowIndex));


        if (StringUtils.isNotEmpty(message)) {
            throw new RuntimeException(message);
        }
        return model;
    }

    public CellParser matchCell(FileParser fileParser, int index) {
        List<CellParser> cells = fileParser.getCellParsers();
        for (int i = 0; i < cells.size(); i++) {
            CellParser cell = cells.get(i);
            if (index + 1 == cell.getIndex()) return cell;
        }
        return null;
    }

}
