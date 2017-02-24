package com.qdb.provmgr.report.newpbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qdb.provmgr.dao.TableModeEnum;
import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.report.PresetContent;
import com.qdb.provmgr.report.ReportHelper;
import com.qdb.provmgr.util.FileUtil;
import com.qdb.provmgr.util.XlsxPOIUtil;

/**
 * @author mashengli
 */
public class NewpbcExcelUtil {

    private static Logger log = LoggerFactory.getLogger(NewpbcExcelUtil.class);

    public static File createExcelFile(TableModeEnum tableMode, String templateFile, String targetFileName, PresetContent presetContent, List<BaseReportEntity> dataList) throws Exception {
        File tempFile = FileUtil.createTempFile(targetFileName);
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(templateFile);
            XSSFWorkbook workbookIn = new XSSFWorkbook(is);
            XSSFSheet sheetIn = workbookIn.getSheetAt(0);

            os = new FileOutputStream(tempFile);
            XSSFWorkbook workbookOut = new XSSFWorkbook();     //创建工作簿

            //汇总报表只有一个sheet
            if ("0".equals(presetContent.getReportType())) {
                XSSFSheet sheetOut = workbookOut.createSheet("COUNT");       //创建sheet
                //拷贝模板
                XlsxPOIUtil.copySheet(sheetIn, sheetOut, workbookIn, workbookOut);
                //填写数据
                writeData(tableMode, sheetOut, presetContent, dataList);
            } else {
                //分行报表sheet个数与账户数目有关
                List<List<BaseReportEntity>> splitBankData = ReportHelper.splitByAD(dataList);
                for (List<BaseReportEntity> baseReportEntityList : splitBankData) {
                    String AD = baseReportEntityList.get(0).getAD();
                    XSSFSheet sheetOut = workbookOut.createSheet(AD);       //创建sheet
                    //拷贝模板
                    XlsxPOIUtil.copySheet(sheetIn, sheetOut, workbookIn, workbookOut);
                    //填写数据
                    writeData(tableMode, sheetOut, presetContent, baseReportEntityList);
                }
            }

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

    public static void writeData(TableModeEnum tableMode, XSSFSheet sheet, PresetContent presetContent, List<BaseReportEntity> dataList) {
        if (TableModeEnum.Table1_1_1.equals(tableMode)) {
            Excel1_1_1.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_1.equals(tableMode)) {
            Excel1_1.writeData(sheet, presetContent, dataList);
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
        if (TableModeEnum.Table1_6_1.equals(tableMode)) {
            Excel1_6_1.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_6.equals(tableMode)) {
            Excel1_6.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_7.equals(tableMode)) {
            Excel1_7.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_8.equals(tableMode)) {
            Excel1_8.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_9_1.equals(tableMode)) {
            Excel1_9_1.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_9.equals(tableMode)) {
            Excel1_9.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_10_1.equals(tableMode)) {
            Excel1_10_1.writeData(sheet, presetContent, dataList);
            return;
        }
        if (TableModeEnum.Table1_10.equals(tableMode)) {
            Excel1_10.writeData(sheet, presetContent, dataList);
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
     * 将分为单位的数格式化为以万元为单位且四舍五入保留6位小数
     * @param bigDecimal
     * @return
     */
    static BigDecimal formatDecimal(BigDecimal bigDecimal) {
        if (null == bigDecimal || new BigDecimal("0").equals(bigDecimal)) {
            return new BigDecimal("0.000000");
        }
        return bigDecimal.divide(new BigDecimal("10000"), 6, BigDecimal.ROUND_HALF_UP);
    }
}
