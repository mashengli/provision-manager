package com.qdb.provmgr.util.constant;

/**
 * Created by wenzhong on 2017/2/20.
 */
public enum NewPbcConvertDestExcelNameEnum {

    TAB_1_1("1_1", "表1-1-1客户备付金入金业务明细表（分账户表）.xlsx"),
    TAB_1_2_1("1_2_1", "表1-2-1支付机构客户备付金出金业务明细表（分账户表）.xlsx"),
    TAB_1_6("1_6 ", "表1-6-1支付机构某银行特殊业务明细表（分账户表）.xlsx"),
    TAB_1_9("1_9", "表1-9-1支付机构客户备付金业务未达账项统计表（分账户表）.xlsx"),
    TAB_1_10("1_10", "表1-10-1支付机构客户备付金业务未达账项分析表（分账户表）.xlsx"),
    TAB_1_1_2("1_1_2", "表1-1客户备付金入金业务明细表.xlsx"),
    TAB_1_2("1_2", "表1-2支付机构客户备付金出金业务明细表.xlsx"),
    TAB_1_3("1_3", "表1-3支付机构客户备付金业务实际出金明细表.xlsx"),
    TAB_1_4("1_4", "表1-4支付机构客户资金账户转账业务统计表.xlsx"),
    TAB_1_5("1_5", "表1-5支付机构客户资金账户余额统计表.xlsx"),
    TAB_1_6_2("1_6_2", "表1-6支付机构某银行特殊业务明细表.xlsx"),
    TAB_1_7("1_7", "表1-7支付机构现金购卡业务.xlsx"),
    TAB_1_8("1_8", "表1-8支付机构预付卡现金赎回业务统计表.xlsx"),
    TAB_1_9_2("1_9_2", "表1-9支付机构客户备付金业务未达账项统计表.xlsx"),
    TAB_1_10_2("1_10_2", "表1-10支付机构客户备付金业务未达账项分析表.xlsx"),
    TAB_1_11("1_11", "表1-11支付机构客户账户资金余额变动调节表.xlsx"),
    TAB_1_12("1_12", "表1-12支付机构客户资金账户余额试算表.xlsx"),
    TAB_1_13("1_13", "表1-13预付卡发行企业备付金账户中售卡押金统计表.xlsx"),;

    private String code;
    private String text;

    NewPbcConvertDestExcelNameEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
