/*
 * Copyright (C) Patpat Online 2024
 * Made with love by Tony Skywalker
 */

package cn.edu.buaa.patpat.boot.common.utils.excel;

import cn.edu.buaa.patpat.boot.common.utils.Medias;
import cn.edu.buaa.patpat.boot.exceptions.BadRequestException;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

import static cn.edu.buaa.patpat.boot.extensions.messages.Messages.M;

public class Excels {
    public static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    private Excels() {}

    /**
     * Try to get cell value as string.
     *
     * @param cell The cell to get value from.
     * @return The cell value as string, or an empty string if the cell is null.
     */
    public static String tryGetCellValue(Cell cell) {
        return tryGetCellValue(cell, "");
    }

    /**
     * Try to get cell value as string.
     *
     * @param cell         The cell to get value from.
     * @param defaultValue The default value to return if the cell is null.
     * @return The cell value as string, or the default value if the cell is null.
     */
    public static String tryGetCellValue(Cell cell, String defaultValue) {
        return cell == null ? defaultValue : cell.getStringCellValue();
    }

    /**
     * Get non-empty cell value as string.
     *
     * @param cell The cell to get value from.
     * @return The non-empty cell value as string.
     * @throws ExcelException If the cell doesn't exist, i.e., null, or the cell is empty.
     */
    public static String getNonEmptyCellValue(Cell cell) throws ExcelException {
        String value = getCellValue(cell);
        if (value.isEmpty()) {
            throw new ExcelException("Cell is empty");
        }
        return value;
    }

    /**
     * Get cell value as string.
     *
     * @param cell The cell to get value from.
     * @return The cell value as string.
     * @throws ExcelException If the cell doesn't exist, i.e., null.
     */
    public static String getCellValue(Cell cell) throws ExcelException {
        if (cell == null) {
            throw new ExcelException("Cell is null");
        }
        // Use DataFormatter to get the cell value as string.
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell);
    }

    public static Workbook getWorkbook() {
        return new XSSFWorkbook();
    }

    public static Workbook getWorkbook(String path) throws IOException {
        String extension = FilenameUtils.getExtension(path);
        if ("xls".equalsIgnoreCase(extension)) {
            return new HSSFWorkbook(Medias.getInputStream(path));
        } else if ("xlsx".equalsIgnoreCase(extension)) {
            return new XSSFWorkbook(Medias.getInputStream(path));
        } else {
            throw new BadRequestException(M("validation.file.type"));
        }
    }
}
