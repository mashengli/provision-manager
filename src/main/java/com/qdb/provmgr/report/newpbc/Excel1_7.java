package com.qdb.provmgr.report.newpbc;

import static com.qdb.provmgr.report.newpbc.NewpbcExcelUtil.formatDecimal;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.dao.entity.report.DataTable1_7;
import com.qdb.provmgr.report.PresetContent;

/**
 * @author mashengli
 */
class Excel1_7 {
    /**
     * 数据区域起始行数下标（下标从0开始）
     */
    static int DATA_START_ROW_NUM = 3;

    /**
     * 数据区域结束行数下标（下标从0开始）
     */
    static int DATA_END_ROW_NUM = 33;

    /**
     * 数据区域起始列数下标（下标从0开始）
     */
    static int DATA_START_COLUMN_NUM = 1;

    /**
     * 数据区域结束列数下标（下标从0开始）
     */
    static int DATA_END_COLUMN_NUM = 4;

    public static void writeData(XSSFSheet sheet, PresetContent presetContent, List<BaseReportEntity> dataList) {
        writeData(sheet, dataList);
    }

    private static void writeData(XSSFSheet sheet, List<BaseReportEntity> dataList) {
        //TODO 此业务暂时不存在，此处直接写死为零，之后若开通请修改此处代码
        for (int i = DATA_START_ROW_NUM; i <= DATA_END_ROW_NUM; i++) {
            for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
                sheet.getRow(i).getCell(j).setCellValue(new BigDecimal("0.000000").doubleValue());
            }
        }
//        if (!CollectionUtils.isEmpty(dataList)) {
//            Collections.sort(dataList);
//            int size = dataList.size();
//            DataTable1_7 total = new DataTable1_7();
//            for (int i = DATA_START_ROW_NUM; i <= DATA_END_ROW_NUM; i++) {
//                if (i - DATA_START_ROW_NUM < size) {
//                    DataTable1_7 dataTable1_7 = (DataTable1_7) dataList.get(i - DATA_START_ROW_NUM);
//                    total = ReportHelper.addData(total, dataTable1_7);
//                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
//                        BigDecimal value = getDoubleDataByColumnIndex(dataTable1_7, j);
//                        sheet.getRow(i).getCell(j).setCellValue(null != value ? value.doubleValue() : new BigDecimal("0.000000").doubleValue());
//                    }
//                } else {
//                    for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
//                        sheet.getRow(i).getCell(j).setCellValue(new BigDecimal("0.000000").doubleValue());
//                    }
//                }
//            }
//            //合计行
//            for (int j = DATA_START_COLUMN_NUM; j <= DATA_END_COLUMN_NUM; j++) {
//                BigDecimal value = getDoubleDataByColumnIndex(total, j);
//                sheet.getRow(DATA_END_ROW_NUM).getCell(j).setCellValue(null != value ? value.doubleValue() : new BigDecimal("0.000000").doubleValue());
//            }
//        }
    }

    /**
     * 获取数据
     *
     * @param dataTable1_7 数据
     * @param index        下标
     */
    private static BigDecimal getDoubleDataByColumnIndex(DataTable1_7 dataTable1_7, int index) {
        switch (index) {
            case 1:
                return formatDecimal(dataTable1_7.getH01());
            case 2:
                return formatDecimal(dataTable1_7.getH02());
            case 3:
                return formatDecimal(dataTable1_7.getH03());
            case 4:
                return formatDecimal(dataTable1_7.getH04());
            default:
                return new BigDecimal("0.000000");
        }
    }
}
