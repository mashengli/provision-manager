package com.qdb.provmgr.report.pbc;

import static com.qdb.provmgr.report.pbc.PbcExcelUtil.formatDecimal;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.dao.entity.report.DataTable1_10;
import com.qdb.provmgr.report.PresetContent;

/**
 * @author mashengli
 */
class Excel1_10 {
    /**
     * 数据区域起始行数下标（下标从0开始）
     */
    static int DATA_START_ROW_NUM = 13;

    /**
     * 数据区域结束行数下标（下标从0开始）
     */
    static int DATA_END_ROW_NUM = 43;

    /**
     * 数据区域起始列数下标（下标从0开始）
     */
    static int DATA_START_COLUMN_NUM = 1;

    /**
     * 数据区域结束列数下标（下标从0开始）
     */
    static int DATA_END_COLUMN_NUM = 24;

    public static void writeData(HSSFSheet sheet, PresetContent presetContent, List<BaseReportEntity> dataList) {
        writePresetContent(sheet, presetContent);
        writeData(sheet, dataList);
    }

    /**
     * 按照模板写数据
     *
     * @param sheet    表格
     * @param dataList 数据列表
     */
    private static void writeData(HSSFSheet sheet, List<BaseReportEntity> dataList) {
        if (!CollectionUtils.isEmpty(dataList)) {
            Collections.sort(dataList);
            for (int i = DATA_START_ROW_NUM; i <= DATA_END_ROW_NUM; i++) {
                if (i - DATA_START_ROW_NUM < dataList.size()) {
                    DataTable1_10 dataTable1_10 = (DataTable1_10) dataList.get(i - DATA_START_ROW_NUM);
                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                        if (j % 2 == 0) {
                            BigDecimal value = getDoubleDataByColumnIndex(dataTable1_10, j);
                            sheet.getRow(i).getCell(j).setCellValue(value.doubleValue());
                        } else {
                            sheet.getRow(i).getCell(j).setCellValue(getNumberDataByColumnIndex(dataTable1_10, j));
                        }
                    }
                } else {
                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                        if (j % 2 == 0) {
                            sheet.getRow(i).getCell(j).setCellValue(0);
                        } else {
                            sheet.getRow(i).getCell(j).setCellValue(0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 填充交易时期、填报日期、填表人及审核人
     *
     * @param sheet         表格
     * @param presetContent 预设内容
     */
    private static void writePresetContent(HSSFSheet sheet, PresetContent presetContent) {
        //填充交易时期、填报日期、填表人及审核人
        sheet.getRow(0).createCell(1).setCellValue(presetContent.getCompanyName());
        sheet.getRow(1).createCell(1).setCellValue(presetContent.getLegalPerson());
        sheet.getRow(2).createCell(1).setCellValue(presetContent.getAuthCompanyName());
        sheet.getRow(3).createCell(1).setCellValue(presetContent.getTranPeriod());
        sheet.getRow(4).createCell(1).setCellValue(presetContent.getBankName());
        sheet.getRow(5).createCell(1).setCellValue(presetContent.getAccountName());
        sheet.getRow(6).createCell(1).setCellValue(presetContent.getAccount());
        sheet.getRow(7).createCell(1).setCellValue(presetContent.getReportDate());
        sheet.getRow(DATA_END_ROW_NUM + 1).createCell(1).setCellValue(presetContent.getReportUserName());
        sheet.getRow(DATA_END_ROW_NUM + 2).createCell(1).setCellValue(presetContent.getCheckUserName());
    }

    /**
     * 获取数据
     *
     * @param dataTable1_10 数据
     * @param index        下标
     */
    private static BigDecimal getDoubleDataByColumnIndex(DataTable1_10 dataTable1_10, int index) {
        switch (index) {
            case 2:
                return formatDecimal(dataTable1_10.getK02());
            case 4:
                return formatDecimal(dataTable1_10.getK04());
            case 6:
                return formatDecimal(dataTable1_10.getK06());
            case 8:
                return formatDecimal(dataTable1_10.getK08());
            case 10:
                return formatDecimal(dataTable1_10.getK10());
            case 12:
                return formatDecimal(dataTable1_10.getK12());
            case 14:
                return formatDecimal(dataTable1_10.getK14());
            case 16:
                return formatDecimal(dataTable1_10.getK16());
            case 18:
                return formatDecimal(dataTable1_10.getK18());
            case 20:
                return formatDecimal(dataTable1_10.getK20());
            case 22:
                return formatDecimal(dataTable1_10.getK22());
            case 24:
                return formatDecimal(dataTable1_10.getK24());
            default:
                return new BigDecimal("0.00");
        }
    }

    private static long getNumberDataByColumnIndex(DataTable1_10 dataTable1_10, int index) {
        switch (index) {
            case 1:
                return formatNumber(dataTable1_10.getK01());
            case 3:
                return formatNumber(dataTable1_10.getK03());
            case 5:
                return formatNumber(dataTable1_10.getK05());
            case 7:
                return formatNumber(dataTable1_10.getK07());
            case 9:
                return formatNumber(dataTable1_10.getK09());
            case 11:
                return formatNumber(dataTable1_10.getK11());
            case 13:
                return formatNumber(dataTable1_10.getK13());
            case 15:
                return formatNumber(dataTable1_10.getK15());
            case 17:
                return formatNumber(dataTable1_10.getK17());
            case 19:
                return formatNumber(dataTable1_10.getK19());
            case 21:
                return formatNumber(dataTable1_10.getK21());
            case 23:
                return formatNumber(dataTable1_10.getK23());
            default:
                return 0L;
        }
    }

    private static long formatNumber(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return 0L;
        }
        return bigDecimal.longValue();
    }
}
