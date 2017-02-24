package com.qdb.provmgr.report.newpbc;

import static com.qdb.provmgr.report.newpbc.NewpbcExcelUtil.formatDecimal;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.dao.entity.report.DataTable1_12;
import com.qdb.provmgr.report.PresetContent;
import com.qdb.provmgr.report.ReportHelper;

/**
 * @author mashengli
 */
class Excel1_12 {
    /**
     * 数据起始行数下标（下标从0开始）
     */
    static int DATA_START_ROW_NUM = 5;

    /**
     * 数据区域结束行数下标（下标从0开始）
     */
    static int DATA_END_ROW_NUM = 35;

    /**
     * 数据起始列数下标（下标从0开始）
     */
    static int DATA_START_COLUMN_NUM = 1;

    /**
     * 数据区域结束数下标（下标从0开始）
     */
    static int DATA_END_COLUMN_NUM = 17;

    public static void writeData(XSSFSheet sheet, PresetContent presetContent, List<BaseReportEntity> dataList) {
        writeData(sheet, ReportHelper.mergeAndSumByDate(dataList));
    }

    /**
     * 按照模板写数据
     *
     * @param sheet    表格
     * @param dataList 数据列表
     */
    private static void writeData(XSSFSheet sheet, List<BaseReportEntity> dataList) {
        if (!CollectionUtils.isEmpty(dataList)) {
            Collections.sort(dataList);
            int size = dataList.size();
            for (int i = DATA_START_ROW_NUM; i <= DATA_END_ROW_NUM; i++) {
                if (i - DATA_START_ROW_NUM < size) {
                    DataTable1_12 dataTable1_12 = (DataTable1_12) dataList.get(i - DATA_START_ROW_NUM);
                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                        BigDecimal value = getDoubleDataByColumnIndex(dataTable1_12, j);
                        sheet.getRow(i).getCell(j).setCellValue(null != value ? value.doubleValue() : new BigDecimal("0.000000").doubleValue());
                    }
                } else {
                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                        sheet.getRow(i).getCell(j).setCellValue(new BigDecimal("0.000000").doubleValue());
                    }
                }
            }
        }
    }

    /**
     * 获取数据
     *
     * @param dataTable1_12 数据
     * @param index        下标
     */
    private static BigDecimal getDoubleDataByColumnIndex(DataTable1_12 dataTable1_12, int index) {
        switch (index) {
            case 1:
                return formatDecimal(dataTable1_12.getM1());
            case 2:
                return formatDecimal(dataTable1_12.getM2());
            case 3:
                return formatDecimal(dataTable1_12.getM3());
            case 4:
                return formatDecimal(dataTable1_12.getM4());
            case 5:
                return formatDecimal(dataTable1_12.getM5());
            case 6:
                return formatDecimal(dataTable1_12.getM6());
            case 7:
                return formatDecimal(dataTable1_12.getM7());
            case 8:
                return formatDecimal(dataTable1_12.getM8());
            case 9:
                return formatDecimal(dataTable1_12.getM9());
            case 10:
                return formatDecimal(dataTable1_12.getM10());
            case 11:
                return new BigDecimal("0.000000");
            case 12:
                return new BigDecimal("0.000000");
            case 13:
                return new BigDecimal("0.000000");
            case 14:
                return formatDecimal(dataTable1_12.getM11());
            case 15:
                return formatDecimal(dataTable1_12.getM12());
            case 16:
                return formatDecimal(dataTable1_12.getM13());
            case 17:
                return formatDecimal(dataTable1_12.getM14());
            default:
                return new BigDecimal("0.000000");
        }
    }
}
