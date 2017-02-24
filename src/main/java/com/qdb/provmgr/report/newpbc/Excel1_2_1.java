package com.qdb.provmgr.report.newpbc;

import static com.qdb.provmgr.report.newpbc.NewpbcExcelUtil.formatDecimal;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.dao.entity.report.DataTable1_2;
import com.qdb.provmgr.report.PresetContent;
import com.qdb.provmgr.report.ReportHelper;

/**
 * @author mashengli
 */
class Excel1_2_1 {

    /**
     * 数据区域起始行数下标（下标从0开始）
     */
    static int DATA_START_ROW_NUM = 6;

    /**
     * 数据区域结束行数下标（下标从0开始）
     */
    static int DATA_END_ROW_NUM = 37;

    /**
     * 数据区域起始列数下标（下标从0开始）
     */
    static int DATA_START_COLUMN_NUM = 1;

    /**
     * 数据区域结束列数下标（下标从0开始）
     */
    static int DATA_END_COLUMN_NUM = 6;

    public static void writeData(XSSFSheet sheet, PresetContent presetContent, List<BaseReportEntity> dataList) {
        writeData(sheet, ReportHelper.mergeAndSumByDate(dataList));
    }

    private static void writeData(XSSFSheet sheet, List<BaseReportEntity> dataList) {
        if (!CollectionUtils.isEmpty(dataList)) {
            Collections.sort(dataList);
            int size = dataList.size();
            DataTable1_2 total = new DataTable1_2();
            for (int i = DATA_START_ROW_NUM; i <= DATA_END_ROW_NUM; i++) {
                if (i - DATA_START_ROW_NUM < size) {
                    DataTable1_2 dataTable1_21 = (DataTable1_2) dataList.get(i - DATA_START_ROW_NUM);
                    total = ReportHelper.addData(total, dataTable1_21);
                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                        BigDecimal value = getDoubleDataByColumnIndex(dataTable1_21, j);
                        sheet.getRow(i).getCell(j).setCellValue(null != value ? value.doubleValue() : new BigDecimal("0.000000").doubleValue());
                    }
                } else {
                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                        sheet.getRow(i).getCell(j).setCellValue(new BigDecimal("0.000000").doubleValue());
                    }
                }
            }
            //合计行
            for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                BigDecimal value = getDoubleDataByColumnIndex(total, j);
                sheet.getRow(DATA_END_ROW_NUM).getCell(j).setCellValue(null != value ? value.doubleValue() : new BigDecimal("0.000000").doubleValue());
            }
        }
    }

    /**
     * 获取数据
     * @param dataTable1_21 数据
     * @param index 下标
     */
    private static BigDecimal getDoubleDataByColumnIndex(DataTable1_2 dataTable1_21, int index) {
        switch (index) {
            case 1:
                return formatDecimal(dataTable1_21.getB04());
            case 2:
                return formatDecimal(dataTable1_21.getB05());
            case 3:
                return formatDecimal(dataTable1_21.getB06());
            case 4:
                return formatDecimal(dataTable1_21.getB07());
            case 5:
                return formatDecimal(dataTable1_21.getB08());
            case 6:
                return formatDecimal(dataTable1_21.getB09());
            default:
                return new BigDecimal("0.000000");
        }
    }
}
