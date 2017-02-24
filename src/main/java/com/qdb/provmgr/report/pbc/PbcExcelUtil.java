package com.qdb.provmgr.report.pbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qdb.provmgr.dao.TableModeEnum;
import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.report.PresetContent;
import com.qdb.provmgr.util.ExcelTemplate;
import com.qdb.provmgr.util.FileUtil;
import com.qdb.provmgr.util.MapUtil;
import com.qdb.provmgr.util.POIUtil;

/**
 * @author mashengli
 */
public class PbcExcelUtil {

    private static Logger log = LoggerFactory.getLogger(PbcExcelUtil.class);

    public static File createExcelFile(TableModeEnum tableMode, String templateFile, String targetFileName, PresetContent presetContent, List<BaseReportEntity> dataList) throws Exception {
        //use excel template to create excel
//        if (TableModeEnum.Table1_1.equals(tableMode) || TableModeEnum.Table1_1_2.equals(tableMode)
//                || TableModeEnum.Table1_2.equals(tableMode) || TableModeEnum.Table1_2_1.equals(tableMode)
//                || TableModeEnum.Table1_4.equals(tableMode) || TableModeEnum.Table1_5.equals(tableMode)
//                || TableModeEnum.Table1_10.equals(tableMode) || TableModeEnum.Table1_10_2.equals(tableMode)
//                || TableModeEnum.Table1_13.equals(tableMode)) {
//            return createExcelByTemplate(templateFile, targetFileName, presetContent, ReportHelper.mergeAndSumByDate(dataList));
//        }
        File tempFile = FileUtil.createTempFile(targetFileName);
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(templateFile);
            HSSFWorkbook workbookIn = new HSSFWorkbook(is);
            HSSFSheet sheetIn = workbookIn.getSheetAt(0);

            os = new FileOutputStream(tempFile);
            HSSFWorkbook workbookOut = new HSSFWorkbook();     //创建工作簿
            HSSFSheet sheetOut = workbookOut.createSheet();       //创建sheet

            //拷贝模板
            POIUtil.copySheet(sheetIn, sheetOut, workbookIn, workbookOut);

            //填写数据
            writeData(tableMode, sheetOut, presetContent, dataList);

            workbookOut.write(os);
            os.flush();
        } catch (Exception e) {
            log.error("创建excel失败");
            throw e;
        } finally {
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        }
        return tempFile;
    }

    /**
     * 根据模板创建文件
     * @param templateFile
     * @param targetFileName
     * @param presetContent
     * @param dataList
     * @return
     */
    private static File createExcelByTemplate(String templateFile, String targetFileName, PresetContent presetContent, List<BaseReportEntity> dataList) {
        File tempFile = FileUtil.createTempFile(targetFileName);
        try {
            ExcelTemplate.createExcel(templateFile, tempFile.getAbsolutePath(), "sheet", dataList, MapUtil.objectToMap(presetContent));
        } catch (Exception e) {
            log.error("创建报表异常", e);
        }
        return tempFile;
    }

    public static void writeData(TableModeEnum tableMode, HSSFSheet sheet, PresetContent presetContent, List<BaseReportEntity> dataList) {
        if (TableModeEnum.Table1_1.equals(tableMode)) {
            Excel1_1.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_1_2.equals(tableMode)) {
            Excel1_1_2.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_2.equals(tableMode)) {
            Excel1_2.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_2_1.equals(tableMode)) {
            Excel1_2_1.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_3.equals(tableMode)) {
            Excel1_3.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_4.equals(tableMode)) {
            Excel1_4.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_5.equals(tableMode)) {
            Excel1_5.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_6.equals(tableMode)) {
            Excel1_6.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_6_2.equals(tableMode)) {
            Excel1_6_2.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_9.equals(tableMode)) {
            Excel1_9.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_9_2.equals(tableMode)) {
            Excel1_9_2.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_10.equals(tableMode)) {
            Excel1_10.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_10_2.equals(tableMode)) {
            Excel1_10_2.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_11.equals(tableMode)) {
            Excel1_11.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_12.equals(tableMode)) {
            Excel1_12.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_13.equals(tableMode)) {
            Excel1_13.writeData(sheet, presetContent, dataList);
        }
    }

    /**
     * 将分为单位的数格式化为以万元为单位且四舍五入保留四位小数
     * @param bigDecimal
     * @return
     */
    static BigDecimal formatDecimal(BigDecimal bigDecimal) {
        if (null == bigDecimal || new BigDecimal("0").equals(bigDecimal)) {
            return new BigDecimal("0.00");
        }
        return bigDecimal.divide(new BigDecimal("10000"), 4, BigDecimal.ROUND_HALF_UP);
    }
}
