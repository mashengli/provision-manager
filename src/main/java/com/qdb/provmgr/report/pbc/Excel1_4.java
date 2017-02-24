package com.qdb.provmgr.report.pbc;

import static com.qdb.provmgr.report.pbc.PbcExcelUtil.formatDecimal;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.dao.entity.report.DataTable1_4;
import com.qdb.provmgr.report.PresetContent;
import com.qdb.provmgr.report.ReportHelper;

/**
 * @author mashengli
 */
class Excel1_4 {

    /**
     * 数据起始行数下标（下标从0开始）
     */
    static int DATA_START_ROW_NUM = 5;

    /**
     * 数据区域结束行数下标（下标从0开始）
     */
    static int DATA_END_ROW_NUM = 36;

    /**
     * 数据起始列数下标（下标从0开始）
     */
    static int DATA_START_COLUMN_NUM = 1;

    /**
     * 数据区域结束行数下标（下标从0开始）
     */
    static int DATA_END_COLUMN_NUM = 4;

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
            DataTable1_4 total = new DataTable1_4();
            for (int i = DATA_START_ROW_NUM; i <= DATA_END_ROW_NUM; i++) {
                if (i - DATA_START_ROW_NUM < size) {
                    DataTable1_4 dataTable1_4 = (DataTable1_4) dataList.get(i - DATA_START_ROW_NUM);
                    total = ReportHelper.addData(total, dataTable1_4);
                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                        BigDecimal value = getDoubleDataByColumnIndex(dataTable1_4, j);
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
     * @param dataTable1_4 数据
     * @param index        下标
     * @return
     */
    private static BigDecimal getDoubleDataByColumnIndex(DataTable1_4 dataTable1_4, int index) {
        switch (index) {
            case 1:
                return formatDecimal(dataTable1_4.getD01());
            case 2:
                return formatDecimal(dataTable1_4.getD02());
            case 3:
                return formatDecimal(dataTable1_4.getD03());
            case 4:
                return formatDecimal(dataTable1_4.getD04());
            default:
                return new BigDecimal("0.00");
        }
    }
}
