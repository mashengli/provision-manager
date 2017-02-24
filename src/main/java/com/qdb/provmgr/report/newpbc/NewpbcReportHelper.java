package com.qdb.provmgr.report.newpbc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qdb.provmgr.dao.TableModeEnum;
import com.qdb.provmgr.report.ReportHelper;

/**
 * @author mashengli
 */
@Component
public class NewpbcReportHelper {

    @Autowired
    private ReportHelper reportHelper;
    
    private static final String PBC_REPORT_ROOT_PATH = "/备付金报表/新版中国人民银行/";

    public static final String FILE_SUFFIX = ".xlsx";

    /**
     * 获取人行报表的文件夹
     * @param timeStr 时间串格式为yyyyMM
     * @return
     */
    public String getPbcFtpDir(String timeStr) {
        return PBC_REPORT_ROOT_PATH + timeStr + "/";
    }

    public String getPbcZipFileName(Date startDate, Date endDate, String companyName) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(startDate) + "_" + simpleDateFormat.format(endDate) + companyName + ".zip";
    }

    /**
     * 上报人行的合作行表集合
     * @return
     */
    public List<TableModeEnum> getPbcReportTablesAccount() {
        List<TableModeEnum> result = new ArrayList<>();
        result.add(TableModeEnum.Table1_1_1);
        result.add(TableModeEnum.Table1_2_1);
        result.add(TableModeEnum.Table1_6_1);
        result.add(TableModeEnum.Table1_9_1);
        result.add(TableModeEnum.Table1_10_1);
        return result;
    }

    /**
     * 上报人行的存管行特殊表集合
     * @return
     */
    public List<TableModeEnum> getPbcReportTablesTotal() {
        List<TableModeEnum> result = new ArrayList<>();
        result.add(TableModeEnum.Table1_1);
        result.add(TableModeEnum.Table1_2);
        result.add(TableModeEnum.Table1_3);
        result.add(TableModeEnum.Table1_4);
        result.add(TableModeEnum.Table1_5);
        result.add(TableModeEnum.Table1_6);
        result.add(TableModeEnum.Table1_7);
        result.add(TableModeEnum.Table1_8);
        result.add(TableModeEnum.Table1_9);
        result.add(TableModeEnum.Table1_10);
        result.add(TableModeEnum.Table1_11);
        result.add(TableModeEnum.Table1_12);
        result.add(TableModeEnum.Table1_13);
        return result;
    }

    /**
     * 上报人行的存管行特殊表集合
     * @return
     */
    public List<TableModeEnum> getPbcReportTablesAll() {
        List<TableModeEnum> result = new ArrayList<>();
        result.add(TableModeEnum.Table1_1_1);
        result.add(TableModeEnum.Table1_2_1);
        result.add(TableModeEnum.Table1_6_1);
        result.add(TableModeEnum.Table1_9_1);
        result.add(TableModeEnum.Table1_10_1);
        result.add(TableModeEnum.Table1_1);
        result.add(TableModeEnum.Table1_2);
        result.add(TableModeEnum.Table1_3);
        result.add(TableModeEnum.Table1_4);
        result.add(TableModeEnum.Table1_5);
        result.add(TableModeEnum.Table1_6);
        result.add(TableModeEnum.Table1_7);
        result.add(TableModeEnum.Table1_8);
        result.add(TableModeEnum.Table1_9);
        result.add(TableModeEnum.Table1_10);
        result.add(TableModeEnum.Table1_11);
        result.add(TableModeEnum.Table1_12);
        result.add(TableModeEnum.Table1_13);
        return result;
    }

    /**
     * 获取模板文件相对WEB-INF下的路径
     * @param tableModeEnum
     * @return
     */
    public String getRelativePbcTemplateFile(TableModeEnum tableModeEnum) {
        if (TableModeEnum.Table1_1.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_1.xlsx";
        }
        if (TableModeEnum.Table1_1_1.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_1_1.xlsx";
        }
        if (TableModeEnum.Table1_2.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_2.xlsx";
        }
        if (TableModeEnum.Table1_2_1.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_2_1.xlsx";
        }
        if (TableModeEnum.Table1_3.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_3.xlsx";
        }
        if (TableModeEnum.Table1_4.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_4.xlsx";
        }
        if (TableModeEnum.Table1_5.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_5.xlsx";
        }
        if (TableModeEnum.Table1_6.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_6.xlsx";
        }
        if (TableModeEnum.Table1_6_1.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_6_1.xlsx";
        }
        if (TableModeEnum.Table1_7.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_7.xlsx";
        }
        if (TableModeEnum.Table1_8.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_8.xlsx";
        }
        if (TableModeEnum.Table1_9.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_9.xlsx";
        }
        if (TableModeEnum.Table1_9_1.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_9_1.xlsx";
        }
        if (TableModeEnum.Table1_10.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_10.xlsx";
        }
        if (TableModeEnum.Table1_10_1.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_10_1.xlsx";
        }
        if (TableModeEnum.Table1_11.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_11.xlsx";
        }
        if (TableModeEnum.Table1_12.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_12.xlsx";
        }
        if (TableModeEnum.Table1_13.equals(tableModeEnum)) {
            return reportHelper.getExcelTemplateDir() + "/newpbc/template_1_13.xlsx";
        }
        return "";
    }

    /**
     * 获取模板文件相对WEB-INF下的路径
     * @param tableModeEnum
     * @return
     */
    public String getPbcFileName(TableModeEnum tableModeEnum) {
        if (TableModeEnum.Table1_1.equals(tableModeEnum)) {
            return "表1-1客户备付金入金业务明细表.xlsx";
        }
        if (TableModeEnum.Table1_1_1.equals(tableModeEnum)) {
            return "表1-1-1客户备付金入金业务明细表（分账户表）.xlsx";
        }
        if (TableModeEnum.Table1_2.equals(tableModeEnum)) {
            return "表1-2支付机构客户备付金出金业务明细表.xlsx";
        }
        if (TableModeEnum.Table1_2_1.equals(tableModeEnum)) {
            return "表1-2-1支付机构客户备付金出金业务明细表（分账户表）.xlsx";
        }
        if (TableModeEnum.Table1_3.equals(tableModeEnum)) {
            return "表1-3支付机构客户备付金业务实际出金明细表.xlsx";
        }
        if (TableModeEnum.Table1_4.equals(tableModeEnum)) {
            return "表1-4支付机构客户资金账户转账业务统计表.xlsx";
        }
        if (TableModeEnum.Table1_5.equals(tableModeEnum)) {
            return "表1-5支付机构客户资金账户余额统计表.xlsx";
        }
        if (TableModeEnum.Table1_6.equals(tableModeEnum)) {
            return "表1-6支付机构某银行特殊业务明细表.xlsx";
        }
        if (TableModeEnum.Table1_6_1.equals(tableModeEnum)) {
            return "表1-6-1支付机构某银行特殊业务明细表（分账户表）.xlsx";
        }
        if (TableModeEnum.Table1_7.equals(tableModeEnum)) {
            return "表1-7支付机构现金购卡业务.xlsx";
        }
        if (TableModeEnum.Table1_8.equals(tableModeEnum)) {
            return "表1-8支付机构预付卡现金赎回业务统计表.xlsx";
        }
        if (TableModeEnum.Table1_9.equals(tableModeEnum)) {
            return "表1-9支付机构客户备付金业务未达账项统计表.xlsx";
        }
        if (TableModeEnum.Table1_9_1.equals(tableModeEnum)) {
            return "表1-9-1支付机构客户备付金业务未达账项统计表（分账户表）.xlsx";
        }
        if (TableModeEnum.Table1_10.equals(tableModeEnum)) {
            return "表1-10支付机构客户备付金业务未达账项分析表.xlsx";
        }
        if (TableModeEnum.Table1_10_1.equals(tableModeEnum)) {
            return "表1-10-1支付机构客户备付金业务未达账项分析表（分账户表）.xlsx";
        }
        if (TableModeEnum.Table1_11.equals(tableModeEnum)) {
            return "表1-11支付机构客户账户资金余额变动调节表.xlsx";
        }
        if (TableModeEnum.Table1_12.equals(tableModeEnum)) {
            return "表1-12支付机构客户资金账户余额试算表.xlsx";
        }
        if (TableModeEnum.Table1_13.equals(tableModeEnum)) {
            return "表1-13预付卡发行企业备付金账户中售卡押金统计表.xlsx";
        }
        return "";
    }

}
