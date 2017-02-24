package com.qdb.provmgr.report.pbc;

import static com.qdb.provmgr.report.pbc.PbcExcelUtil.formatDecimal;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.dao.entity.report.DataTable1_1;
import com.qdb.provmgr.report.PresetContent;
import com.qdb.provmgr.report.ReportHelper;

/**
 * @author mashengli
 */
class Excel1_1 {

    /**
     * 数据区域起始行数下标（下标从0开始）
     */
    static int DATA_START_ROW_NUM = 15;

    /**
     * 数据区域结束行数下标（下标从0开始）
     */
    static int DATA_END_ROW_NUM = 46;

    /**
     * 数据区域起始列数下标（下标从0开始）
     */
    static int DATA_START_COLUMN_NUM = 1;

    /**
     * 数据区域结束列数下标（下标从0开始）
     */
    static int DATA_END_COLUMN_NUM = 22;

    public static void writeData(HSSFSheet sheet, PresetContent presetContent, List<BaseReportEntity> dataList) {
        writePresetContent(sheet, presetContent);
        writeData(sheet, dataList);
    }

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

    private static void writeData(HSSFSheet sheet, List<BaseReportEntity> dataList) {
        if (!CollectionUtils.isEmpty(dataList)) {
            Collections.sort(dataList);
            DataTable1_1 total = new DataTable1_1();
            for (int i = DATA_START_ROW_NUM; i <= DATA_END_ROW_NUM; i++) {
                if (i - DATA_START_ROW_NUM < dataList.size()) {
                    DataTable1_1 dataTable1_1 = (DataTable1_1) dataList.get(i - DATA_START_ROW_NUM);
                    total = ReportHelper.addData(total, dataTable1_1);
                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                        BigDecimal value = getDoubleDataByColumnIndex(dataTable1_1, j);
                        sheet.getRow(i).getCell(j).setCellValue(null != value ? value.doubleValue() : 0);
                    }
                } else {
                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                        sheet.getRow(i).getCell(j).setCellValue(0);
                    }
                }
            }
            //合计行
            for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                BigDecimal value = getDoubleDataByColumnIndex(total, j);
                sheet.getRow(DATA_END_ROW_NUM).getCell(j).setCellValue(null != value ? value.doubleValue() : 0);
            }
        }
    }

    /**
     * 获取数据
     *
     * @param dataTable1_1 数据
     * @param index        下标
     * @return 数据
     */
    private static BigDecimal getDoubleDataByColumnIndex(DataTable1_1 dataTable1_1, int index) {
        switch (index) {
            case 1:
                return formatDecimal(dataTable1_1.getA01());
            case 2:
                return formatDecimal(dataTable1_1.getA02());
            case 3:
                return formatDecimal(dataTable1_1.getA03());
            case 4:
                return formatDecimal(dataTable1_1.getA0301());
            case 5:
                return formatDecimal(dataTable1_1.getA0302());
            case 6:
                return formatDecimal(dataTable1_1.getA04());
            case 7:
                return formatDecimal(dataTable1_1.getA05());
            case 8:
                return formatDecimal(dataTable1_1.getA06());
            case 9:
                return formatDecimal(dataTable1_1.getA0601());
            case 10:
                return formatDecimal(dataTable1_1.getA0602());
            case 11:
                return formatDecimal(dataTable1_1.getA07());
            case 12:
                return formatDecimal(dataTable1_1.getA08());
            case 13:
                return formatDecimal(dataTable1_1.getA09());
            case 14:
                return formatDecimal(dataTable1_1.getA0901());
            case 15:
                return formatDecimal(dataTable1_1.getA0902());
            case 16:
                return formatDecimal(dataTable1_1.getA10());
            case 17:
                return formatDecimal(dataTable1_1.getA11());
            case 18:
                return formatDecimal(dataTable1_1.getA12());
            case 19:
                return formatDecimal(dataTable1_1.getA13());
            case 20:
                return formatDecimal(dataTable1_1.getA1301());
            case 21:
                return formatDecimal(dataTable1_1.getA1302());
            case 22:
                return formatDecimal(dataTable1_1.getA14());
            default:
                return new BigDecimal("0.00");
        }
    }

}
