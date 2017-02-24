package com.qdb.provmgr.report.newpbc;

import static com.qdb.provmgr.report.newpbc.NewpbcExcelUtil.formatDecimal;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.dao.entity.report.DataTable1_3;
import com.qdb.provmgr.report.DecimalTool;
import com.qdb.provmgr.report.PresetContent;

/**
 * @author mashengli
 */
class Excel1_3 {

    /**
     * 数据起始行数下标（下标从0开始）
     */
    static int DATA_START_ROW_NUM = 2;

    /**
     * 数据起始列数下标（下标从0开始）
     */
    static int DATA_START_COLUMN_NUM = 3;

    /**
     * 数据结束列数下标（下标从0开始）
     */
    static int DATA_END_COLUMN_NUM = 33;

    public static void writeData(XSSFSheet sheet, PresetContent presetContent, List<BaseReportEntity> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        Map<String, LinkedHashMap> dataMap = convertData(dataList);
        int rowNum = DATA_START_ROW_NUM;
        //保存合计的数值
        LinkedHashMap totalDataMap = dataMap.get("total");
        dataMap.remove("total");
        int countAccount = 0;//计数，用于计算实际账户数量
        for (LinkedHashMap rowData : dataMap.values()) {
            countAccount++;
            int column = 0;
            XSSFRow row = sheet.createRow(rowNum++);
            Object[] values = rowData.values().toArray();
            for (int i = 0; i <= DATA_END_COLUMN_NUM; i++) {
                if (i < values.length) {
                    if (column < DATA_START_COLUMN_NUM) {
                        row.createCell(column++).setCellValue((String)values[i]);
                    } else {
                        row.createCell(column++).setCellValue(null != values[i] ? ((BigDecimal)values[i]).doubleValue() : new BigDecimal("0.000000").doubleValue());
                    }
                } else {
                    row.createCell(column++).setCellValue(new BigDecimal("0.000000").doubleValue());
                }
            }
        }

        XSSFRow totalRow = sheet.createRow(DATA_START_ROW_NUM + countAccount);
        int columnNum = 0;
        totalRow.createCell(columnNum++).setCellValue("合计");
        totalRow.createCell(columnNum++);
        totalRow.createCell(columnNum++);
        sheet.addMergedRegion(new CellRangeAddress(DATA_START_ROW_NUM + countAccount, DATA_START_ROW_NUM + countAccount, 0, 2));

        Object[] values = totalDataMap.values().toArray();
        for (int i = columnNum; i <= DATA_END_COLUMN_NUM; i++) {
            if (i - columnNum < values.length) {
                totalRow.createCell(i).setCellValue(null != values[i - columnNum] ? ((BigDecimal)values[i - columnNum]).doubleValue() : new BigDecimal("0.000000").doubleValue());
            } else {
                totalRow.createCell(i).setCellValue(new BigDecimal("0.000000").doubleValue());
            }
        }
    }

    /**
     * 数据转化
     * @param dataList 源列表数据
     * @return map
     */
    private static Map<String, LinkedHashMap> convertData(List<BaseReportEntity> dataList) {
        Map<String, LinkedHashMap> resultMap = new HashMap<>();
        //按照日期进行排序
        Collections.sort(dataList);
        //合计数据，计算各个账户同一天累加的数据,只填数据
        LinkedHashMap<String, BigDecimal> totalMap = new LinkedHashMap<>();

        for (BaseReportEntity baseReportEntity : dataList) {
            DataTable1_3 data = (DataTable1_3) baseReportEntity;
            if (totalMap.containsKey(data.getNatuDate())) {
                BigDecimal newValue = DecimalTool.add(totalMap.get(data.getNatuDate()), formatDecimal(data.getC01()));
                totalMap.put(data.getNatuDate(), newValue);
            } else {
                totalMap.put(data.getNatuDate(), formatDecimal(data.getC01()));
            }
            //按照账户进行分组，将同一账户的不同日期进行行列转换，并按照日期顺序进行赋值
            if (resultMap.containsKey(data.getAD())) {
                LinkedHashMap<String, Object> row = resultMap.get(data.getAD());
                row.put(data.getNatuDate(), formatDecimal(data.getC01()));
            } else {
                LinkedHashMap<String, Object> row = new LinkedHashMap<>();
                row.put("bankName", data.getBankName());
                row.put("name", data.getName());
                row.put("AD", data.getAD());
                row.put(data.getNatuDate(), formatDecimal(data.getC01()));
                resultMap.put(data.getAD(), row);
            }
        }
        resultMap.put("total", totalMap);
        return resultMap;
    }
}
