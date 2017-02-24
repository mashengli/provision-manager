package com.qdb.provmgr.report.newpbc;

import static com.qdb.provmgr.report.newpbc.NewpbcExcelUtil.formatDecimal;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.dao.entity.report.DataTable1_13;
import com.qdb.provmgr.report.PresetContent;
import com.qdb.provmgr.report.ReportHelper;

/**
 * @author mashengli
 */
class Excel1_13 {
    /**
     * 数据区域起始行数下标（下标从0开始）
     */
    static int DATA_START_ROW_NUM = 4;

    /**
     * 数据区域结束行数下标（下标从0开始）
     */
    static int DATA_END_ROW_NUM = 34;

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

    /**
     * 写数据
     * @param sheet 表格
     * @param dataList 数据
     */
    private static void  writeData(XSSFSheet sheet, List<BaseReportEntity> dataList) {
        if (!CollectionUtils.isEmpty(dataList)) {
            Collections.sort(dataList);
            int size = dataList.size();
            for (int i = DATA_START_ROW_NUM; i <= DATA_END_ROW_NUM; i++) {
                if (i - DATA_START_ROW_NUM < size) {
                    DataTable1_13 dataTable1_13 = (DataTable1_13) dataList.get(i - DATA_START_ROW_NUM);
                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                        BigDecimal value = getDoubleDataByColumnIndex(dataTable1_13, j);
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
     * @param dataTable1_13 数据
     * @param index 下标
     */
    private static BigDecimal getDoubleDataByColumnIndex(DataTable1_13 dataTable1_13, int index) {
        switch (index) {
            case 1:
                return formatDecimal(dataTable1_13.getN01());
            case 2:
                return formatDecimal(dataTable1_13.getN02());
            case 3:
                return formatDecimal(dataTable1_13.getN03());
            case 4:
                return formatDecimal(dataTable1_13.getN04());
            case 5:
                return formatDecimal(dataTable1_13.getN05());
            case 6:
                return formatDecimal(dataTable1_13.getN06());
            default:
                return new BigDecimal("0.000000");
        }
    }
}
