package com.qdb.provmgr.util;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author mashengli
 */
public class XlsxPOIUtil {

    /**
     * 功能：拷贝sheet
     *
     * @param sourceSheet 源表格
     * @param targetSheet 目标表格
     * @param sourceWork  原工作簿
     * @param targetWork  目标工作簿
     */
    public static void copySheet(XSSFSheet sourceSheet, XSSFSheet targetSheet,
                                 XSSFWorkbook sourceWork, XSSFWorkbook targetWork) throws Exception {
        if (targetSheet == null || sourceSheet == null || targetWork == null || sourceWork == null) {
            throw new IllegalArgumentException("调用PoiUtil.copySheet()" +
                    "方法时，targetSheet、sourceSheet、targetWork、sourceWork都不能为空，故抛出该异常！");
        }

        //复制源表中的行
        int maxColumnNum = 0;

        for (int i = sourceSheet.getFirstRowNum(); i <= sourceSheet.getLastRowNum(); i++) {
            XSSFRow sourceRow = sourceSheet.getRow(i);
            XSSFRow targetRow = targetSheet.createRow(i);

            if (sourceRow != null) {
                copyRow(sourceRow, targetRow, sourceWork, targetWork);
                if (sourceRow.getLastCellNum() > maxColumnNum) {
                    maxColumnNum = sourceRow.getLastCellNum();
                }
            }
        }

        //复制源表中的合并单元格
        mergerRegion(sourceSheet, targetSheet);

        //设置目标sheet的列宽
        for (int i = 0; i <= maxColumnNum; i++) {
            targetSheet.setColumnWidth(i, sourceSheet.getColumnWidth(i));
        }
    }

    /**
     * 功能：拷贝row
     *
     * @param sourceRow       源行数据
     * @param targetRow       目标行数据
     * @param sourceWork      源工作簿
     * @param targetWork      目标工作簿
     */
    public static void copyRow(XSSFRow sourceRow, XSSFRow targetRow,
                               XSSFWorkbook sourceWork, XSSFWorkbook targetWork) throws Exception {
        if (targetRow == null || sourceRow == null || targetWork == null || sourceWork == null) {
            throw new IllegalArgumentException("调用PoiUtil.copyRow()" +
                    "方法时，targetRow、sourceRow、targetWork、sourceWork、targetPatriarch都不能为空，故抛出该异常！");
        }

        //设置行高
        targetRow.setHeight(sourceRow.getHeight());

        for (int i = sourceRow.getFirstCellNum(); i <= sourceRow.getLastCellNum(); i++) {
            XSSFCell sourceCell = sourceRow.getCell(i);
            XSSFCell targetCell = targetRow.getCell(i);

            if (sourceCell != null) {
                if (targetCell == null) {
                    targetCell = targetRow.createCell(i);
                }

                //拷贝单元格，包括内容和样式
                copyCell(sourceCell, targetCell, sourceWork, targetWork);
            }
        }
    }

    /**
     * 功能：拷贝cell，依据styleMap是否为空判断是否拷贝单元格样式
     *
     * @param sourceCell 源单元格
     * @param targetCell 目标单元格
     * @param sourceWork 源工作簿
     * @param targetWork 目标工作簿
     */
    public static void copyCell(XSSFCell sourceCell, XSSFCell targetCell, XSSFWorkbook sourceWork, XSSFWorkbook
            targetWork) {
        if (targetCell == null || sourceCell == null || targetWork == null || sourceWork == null) {
            throw new IllegalArgumentException("调用PoiUtil.copyCell()" +
                    "方法时，targetCell、sourceCell、targetWork、sourceWork都不能为空，故抛出该异常！");
        }

        //处理单元格内容
        switch (sourceCell.getCellType()) {
            case XSSFCell.CELL_TYPE_STRING:
                targetCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                targetCell.setCellValue(sourceCell.getNumericCellValue());
                break;
            case XSSFCell.CELL_TYPE_BLANK:
                targetCell.setCellType(XSSFCell.CELL_TYPE_BLANK);
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case XSSFCell.CELL_TYPE_ERROR:
                targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
                break;
            case XSSFCell.CELL_TYPE_FORMULA:
                targetCell.setCellFormula(sourceCell.getCellFormula());
                break;
            default:
                break;
        }
    }

    /**
     * 功能：复制原有sheet的合并单元格到新创建的sheet
     *
     * @param sourceSheet 源单元格
     * @param targetSheet 目标单元格
     */
    public static void mergerRegion(XSSFSheet sourceSheet, XSSFSheet targetSheet) throws Exception {
        if (targetSheet == null || sourceSheet == null) {
            throw new IllegalArgumentException("调用PoiUtil.mergerRegion()方法时，targetSheet或者sourceSheet不能为空，故抛出该异常！");
        }

        for (int i = 0; i < sourceSheet.getNumMergedRegions(); i++) {
            CellRangeAddress oldRange = sourceSheet.getMergedRegion(i);
            CellRangeAddress newRange = new CellRangeAddress(
                    oldRange.getFirstRow(), oldRange.getLastRow(),
                    oldRange.getFirstColumn(), oldRange.getLastColumn());
            targetSheet.addMergedRegion(newRange);
        }
    }

}
