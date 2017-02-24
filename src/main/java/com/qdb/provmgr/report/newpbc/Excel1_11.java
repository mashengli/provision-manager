package com.qdb.provmgr.report.newpbc;

import static com.qdb.provmgr.report.newpbc.NewpbcExcelUtil.formatDecimal;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.dao.entity.report.DataTable1_11;
import com.qdb.provmgr.report.PresetContent;
import com.qdb.provmgr.report.ReportHelper;

/**
 * @author mashengli
 */
class Excel1_11 {
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
    static int DATA_END_COLUMN_NUM = 27;

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
                    DataTable1_11 dataTable1_11 = (DataTable1_11) dataList.get(i - DATA_START_ROW_NUM);
                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                        BigDecimal value = getDoubleDataByRowIndex(dataTable1_11, j);
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
     * @param dataTable1_11 数据
     * @param index        下标
     */
    private static BigDecimal getDoubleDataByRowIndex(DataTable1_11 dataTable1_11, int index) {
        switch (index) {
            case 1:
                return formatDecimal(dataTable1_11.getL1());
            case 2:
                return formatDecimal(dataTable1_11.getL2());
            case 3:
                return formatDecimal(dataTable1_11.getL3());
            case 4:
                return formatDecimal(dataTable1_11.getL4());
            case 5:
                return formatDecimal(dataTable1_11.getL5());
            case 6:
                return formatDecimal(dataTable1_11.getL6());
            case 7:
                return formatDecimal(dataTable1_11.getL7());
            case 8:
                return formatDecimal(dataTable1_11.getL8());
            case 9:
                return formatDecimal(dataTable1_11.getL9());
            case 10:
                return formatDecimal(dataTable1_11.getL10());
            case 11:
                return formatDecimal(dataTable1_11.getL11());
            case 12:
                return formatDecimal(dataTable1_11.getL12());
            case 13:
                return formatDecimal(dataTable1_11.getL13());
            case 14:
                return formatDecimal(dataTable1_11.getL14());
            case 15:
                return formatDecimal(dataTable1_11.getL15());
            case 16:
                return formatDecimal(dataTable1_11.getL16());
            case 17:
                return formatDecimal(dataTable1_11.getL17());
            case 18:
                return formatDecimal(dataTable1_11.getL18());
            case 19:
                return formatDecimal(dataTable1_11.getL19());
            case 20:
                return new BigDecimal("0.000000");
            case 21:
                return new BigDecimal("0.000000");
            case 22:
                return new BigDecimal("0.000000");
            case 23:
                return formatDecimal(dataTable1_11.getL20());
            case 24:
                return formatDecimal(dataTable1_11.getL21());
            case 25:
                return formatDecimal(dataTable1_11.getL22());
            case 26:
                return formatDecimal(dataTable1_11.getL23());
            case 27:
                return formatDecimal(dataTable1_11.getL24());
            default:
                return new BigDecimal("0.000000");
        }
    }
}
