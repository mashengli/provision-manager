package com.qdb.provmgr.report.newpbc;

import static com.qdb.provmgr.report.newpbc.NewpbcExcelUtil.formatDecimal;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.dao.entity.report.DataTable1_6;
import com.qdb.provmgr.report.PresetContent;
import com.qdb.provmgr.report.ReportHelper;

/**
 * @author mashengli
 */
class Excel1_6 {

    /**
     * 数据起始行数下标（下标从0开始）
     */
    static int DATA_START_ROW_NUM = 7;

    /**
     * 数据区域结束行数下标（下标从0开始）
     */
    static int DATA_END_ROW_NUM = 37;

    /**
     * 数据起始列数下标（下标从0开始）
     */
    static int DATA_START_COLUMN_NUM = 1;

    /**
     * 数据区域结束数下标（下标从0开始）
     */
    static int DATA_END_COLUMN_NUM = 25;

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
            for (int i = DATA_START_ROW_NUM; i <= DATA_END_ROW_NUM; i++) {
                if (i - DATA_START_ROW_NUM < dataList.size()) {
                    DataTable1_6 dataTable1_6 = (DataTable1_6) dataList.get(i - DATA_START_ROW_NUM);
                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                        BigDecimal value = getDoubleDataByColumnIndex(dataTable1_6, j);
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
     * @param dataTable1_6 数据
     * @param index        下标
     */
    private static BigDecimal getDoubleDataByColumnIndex(DataTable1_6 dataTable1_6, int index) {
        switch (index) {
            case 1:
                return formatDecimal(dataTable1_6.getF01());
            case 2:
                return formatDecimal(dataTable1_6.getF02());
            case 3:
                return formatDecimal(dataTable1_6.getF03());
            case 4:
                return formatDecimal(dataTable1_6.getF04());
            case 5:
                return formatDecimal(dataTable1_6.getF05());
            case 6:
                return formatDecimal(dataTable1_6.getF06());
            case 7:
                return formatDecimal(dataTable1_6.getF07());
            case 8:
                return formatDecimal(dataTable1_6.getF08());
            case 9:
                return formatDecimal(dataTable1_6.getF09());
            case 10:
                return new BigDecimal("0.000000");
            case 11:
                return formatDecimal(dataTable1_6.getF10());
            case 12:
                return formatDecimal(dataTable1_6.getG01());
            case 13:
                return formatDecimal(dataTable1_6.getG02());
            case 14:
                return formatDecimal(dataTable1_6.getG03());
            case 15:
                return formatDecimal(dataTable1_6.getG04());
            case 16:
                return formatDecimal(dataTable1_6.getG05());
            case 17:
                return formatDecimal(dataTable1_6.getG06());
            case 18:
                return formatDecimal(dataTable1_6.getG07());
            case 19:
                return formatDecimal(dataTable1_6.getG08());
            case 20:
                return formatDecimal(dataTable1_6.getG09());
            case 21:
                return formatDecimal(dataTable1_6.getG10());
            case 22:
                return formatDecimal(dataTable1_6.getG11());
            case 23:
                return formatDecimal(dataTable1_6.getG12());
            case 24:
                return formatDecimal(dataTable1_6.getG13());
            case 25:
                return formatDecimal(dataTable1_6.getG14());
            default:
                return new BigDecimal("0.000000");
        }
    }
}
