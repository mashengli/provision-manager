package com.qdb.provmgr.report.newpbc;

import static com.qdb.provmgr.report.newpbc.NewpbcExcelUtil.formatDecimal;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.dao.entity.report.DataTable1_1;
import com.qdb.provmgr.report.PresetContent;
import com.qdb.provmgr.report.ReportHelper;

/**
 * @author mashengli
 */
class Excel1_1_1 {

    /**
     * 数据区域起始行数下标（下标从0开始）
     */
    static int DATA_START_ROW_NUM = 9;

    /**
     * 数据区域结束行数下标（下标从0开始）
     */
    static int DATA_END_ROW_NUM = 40;

    /**
     * 数据区域起始列数下标（下标从0开始）
     */
    static int DATA_START_COLUMN_NUM = 1;

    /**
     * 数据区域结束列数下标（下标从0开始）
     */
    static int DATA_END_COLUMN_NUM = 19;

    public static void writeData(XSSFSheet sheet, PresetContent presetContent, List<BaseReportEntity> dataList) {
        writeData(sheet, ReportHelper.mergeAndSumByDate(dataList));
    }

    private static void writeData(XSSFSheet sheet, List<BaseReportEntity> dataList) {
        if (!CollectionUtils.isEmpty(dataList)) {
            Collections.sort(dataList);
            DataTable1_1 total = new DataTable1_1();
            for (int i = DATA_START_ROW_NUM; i <= DATA_END_ROW_NUM; i++) {
                if (i - DATA_START_ROW_NUM < dataList.size()) {
                    DataTable1_1 dataTable1_1 = (DataTable1_1) dataList.get(i - DATA_START_ROW_NUM);
                    total = ReportHelper.addData(total, dataTable1_1);
                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                        BigDecimal value = getDoubleDataByColumnIndex(dataTable1_1, j);
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
                return formatDecimal(dataTable1_1.getA07());
            case 11:
                return formatDecimal(dataTable1_1.getA08());
            case 12:
                return formatDecimal(dataTable1_1.getA09());
            case 13:
                return formatDecimal(dataTable1_1.getA0901());
            case 14:
                return formatDecimal(dataTable1_1.getA10());
            case 15:
                return formatDecimal(dataTable1_1.getA11());
            case 16:
                return formatDecimal(dataTable1_1.getA12());
            case 17:
                return formatDecimal(dataTable1_1.getA13());
            case 18:
                return formatDecimal(dataTable1_1.getA1301());
            case 19:
                return formatDecimal(dataTable1_1.getA14());
            default:
                return new BigDecimal("0.000000");
        }
    }

}
