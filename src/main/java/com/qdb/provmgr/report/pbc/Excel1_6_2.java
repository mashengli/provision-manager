package com.qdb.provmgr.report.pbc;

import static com.qdb.provmgr.report.pbc.PbcExcelUtil.formatDecimal;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.dao.entity.report.DataTable1_6;
import com.qdb.provmgr.report.PresetContent;
import com.qdb.provmgr.report.ReportHelper;

/**
 * @author mashengli
 */
class Excel1_6_2 {

    /**
     * 数据起始行数下标（下标从0开始）
     */
    static int DATA_START_ROW_NUM = 4;

    /**
     * 数据区域结束行数下标（下标从0开始）
     */
    static int DATA_END_ROW_NUM = 31;

    /**
     * 数据起始列数下标（下标从0开始）
     */
    static int DATA_START_COLUMN_NUM = 2;

    /**
     * 数据区域结束数下标（下标从0开始）
     */
    static int DATA_END_COLUMN_NUM = 32;

    public static void writeData(HSSFSheet sheet, PresetContent presetContent, List<BaseReportEntity> dataList) {
        writePresetContent(sheet, presetContent);
        writeData(sheet, ReportHelper.mergeAndSumByDate(dataList));
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
            int size = dataList.size();
            for (int i = DATA_START_COLUMN_NUM; i <= DATA_END_COLUMN_NUM; i++) {
                if (i - DATA_START_COLUMN_NUM < size) {
                    DataTable1_6 dataTable1_6 = (DataTable1_6) dataList.get(i - DATA_START_COLUMN_NUM);
                    for (int j = DATA_START_ROW_NUM; j <= DATA_END_ROW_NUM; j++) {
                        BigDecimal value = getDoubleDataByRowIndex(dataTable1_6, j);
                        sheet.getRow(j).getCell(i).setCellValue(null != value ? value.doubleValue() : 0);
                    }
                } else {
                    for (int j = DATA_START_ROW_NUM; j <= DATA_END_ROW_NUM; j++) {
                        sheet.getRow(j).getCell(i).setCellValue(0);
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
        sheet.getRow(1).createCell(1).setCellValue(presetContent.getTranPeriod());
        sheet.getRow(2).createCell(1).setCellValue(presetContent.getReportDate());
        sheet.getRow(DATA_END_ROW_NUM + 1).createCell(1).setCellValue(presetContent.getReportUserName());
        sheet.getRow(DATA_END_ROW_NUM + 2).createCell(1).setCellValue(presetContent.getCheckUserName());
    }

    /**
     * 获取数据
     *
     * @param dataTable1_6 数据
     * @param index        下标
     */
    private static BigDecimal getDoubleDataByRowIndex(DataTable1_6 dataTable1_6, int index) {
        switch (index) {
            case 4:
                return new BigDecimal("0.00");
            case 5:
                return formatDecimal(dataTable1_6.getF01());
            case 6:
                return formatDecimal(dataTable1_6.getF02());
            case 7:
                return formatDecimal(dataTable1_6.getF03());
            case 8:
                return formatDecimal(dataTable1_6.getF04());
            case 9:
                return formatDecimal(dataTable1_6.getF05());
            case 10:
                return formatDecimal(dataTable1_6.getF06());
            case 11:
                return formatDecimal(dataTable1_6.getF07());
            case 12:
                return new BigDecimal("0.00");
            case 13:
                return formatDecimal(dataTable1_6.getF08());
            case 14:
                return formatDecimal(dataTable1_6.getF09());
            case 15:
                return formatDecimal(dataTable1_6.getF10());
            case 16:
                return new BigDecimal("0.00");
            case 17:
                return formatDecimal(dataTable1_6.getG01());
            case 18:
                return formatDecimal(dataTable1_6.getG02());
            case 19:
                return formatDecimal(dataTable1_6.getG03());
            case 20:
                return formatDecimal(dataTable1_6.getG04());
            case 21:
                return formatDecimal(dataTable1_6.getG05());
            case 22:
                return formatDecimal(dataTable1_6.getG06());
            case 23:
                return formatDecimal(dataTable1_6.getG07());
            case 24:
                return formatDecimal(dataTable1_6.getG08());
            case 25:
                return formatDecimal(dataTable1_6.getG09());
            case 26:
                return formatDecimal(dataTable1_6.getG10());
            case 27:
                return new BigDecimal("0.00");
            case 28:
                return formatDecimal(dataTable1_6.getG11());
            case 29:
                return formatDecimal(dataTable1_6.getG12());
            case 30:
                return formatDecimal(dataTable1_6.getG13());
            case 31:
                return formatDecimal(dataTable1_6.getG14());
            default:
                return new BigDecimal("0.00");
        }
    }
}
