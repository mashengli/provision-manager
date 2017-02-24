package com.qdb.provmgr.util.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenzhong on 2017/2/21.
 */
public interface ReportConvertConstant {

    String ROOT_DIR = "/Users/wenzhong/report/";
    String FINAL_PATH = "/Users/wenzhong/dest/";


     String TAB_1_1 = "1_1";
     String TAB_1_2_1 = "1_2_1";
     String TAB_1_6 = "1_6 ";
     String TAB_1_9 = "1_9";
     String TAB_1_10 = "1_10";
     String TAB_1_1_2 = "1_1_2";
     String TAB_1_2 = "1_2";
     String TAB_1_3 = "1_3";
     String TAB_1_4 = "1_4";
     String TAB_1_5 = "1_5";
     String TAB_1_6_2 = "1_6_2";
     String TAB_1_7 = "1_7";
     String TAB_1_8 = "1_8";
     String TAB_1_9_2 = "1_9_2";
     String TAB_1_10_2 = "1_10_2";
     String TAB_1_11 = "1_11";
     String TAB_1_12 = "1_12";
     String TAB_1_13 = "1_13";

     //读取源文件
     Map<String, ReportConvertSourcesEnum> srcMap = new HashMap(){
         {
             this.put(TAB_1_1, ReportConvertSourcesEnum.TAB_1_1);
             this.put(TAB_1_2_1, ReportConvertSourcesEnum.TAB_1_2_1);
             this.put(TAB_1_6, ReportConvertSourcesEnum. TAB_1_6);
             this.put(TAB_1_9, ReportConvertSourcesEnum.TAB_1_9);
             this.put(TAB_1_10, ReportConvertSourcesEnum.TAB_1_10);
             this.put(TAB_1_1_2, ReportConvertSourcesEnum.TAB_1_1_2);
             this.put(TAB_1_2, ReportConvertSourcesEnum.TAB_1_2);
             this.put(TAB_1_3, ReportConvertSourcesEnum.TAB_1_3);
             this.put(TAB_1_4, ReportConvertSourcesEnum.TAB_1_4);
             this.put(TAB_1_5, ReportConvertSourcesEnum.TAB_1_5);
             this.put(TAB_1_6_2, ReportConvertSourcesEnum.TAB_1_6_2);
             this.put(TAB_1_7, ReportConvertSourcesEnum.TAB_1_7);
             this.put(TAB_1_8, ReportConvertSourcesEnum.TAB_1_8);
             this.put(TAB_1_9_2, ReportConvertSourcesEnum.TAB_1_9_2);
             this.put(TAB_1_10_2, ReportConvertSourcesEnum.TAB_1_10_2);
             this.put(TAB_1_11, ReportConvertSourcesEnum.TAB_1_11);
             this.put(TAB_1_12, ReportConvertSourcesEnum.TAB_1_12);
             this.put(TAB_1_13, ReportConvertSourcesEnum.TAB_1_13);
         }
     };


     //目标模板
     Map<String, NewPbcConvertDestExcelNameEnum> destNameMap = new HashMap(){
         {
             this.put(TAB_1_1, NewPbcConvertDestExcelNameEnum.TAB_1_1);
             this.put(TAB_1_2_1, NewPbcConvertDestExcelNameEnum.TAB_1_2_1);
             this.put(TAB_1_6, NewPbcConvertDestExcelNameEnum.TAB_1_6);
             this.put(TAB_1_9, NewPbcConvertDestExcelNameEnum.TAB_1_9);
             this.put(TAB_1_10, NewPbcConvertDestExcelNameEnum.TAB_1_10);
             this.put(TAB_1_1_2, NewPbcConvertDestExcelNameEnum.TAB_1_1_2);
             this.put(TAB_1_2, NewPbcConvertDestExcelNameEnum.TAB_1_2);
             this.put(TAB_1_3, NewPbcConvertDestExcelNameEnum.TAB_1_3);
             this.put(TAB_1_4, NewPbcConvertDestExcelNameEnum.TAB_1_4);
             this.put(TAB_1_5, NewPbcConvertDestExcelNameEnum.TAB_1_5);
             this.put(TAB_1_6_2, NewPbcConvertDestExcelNameEnum.TAB_1_6_2);
             this.put(TAB_1_7, NewPbcConvertDestExcelNameEnum.TAB_1_7);
             this.put(TAB_1_8, NewPbcConvertDestExcelNameEnum.TAB_1_8);
             this.put(TAB_1_9_2, NewPbcConvertDestExcelNameEnum.TAB_1_9_2);
             this.put(TAB_1_10_2, NewPbcConvertDestExcelNameEnum.TAB_1_10_2);
             this.put(TAB_1_11, NewPbcConvertDestExcelNameEnum.TAB_1_11);
             this.put(TAB_1_12, NewPbcConvertDestExcelNameEnum.TAB_1_12);
             this.put(TAB_1_13, NewPbcConvertDestExcelNameEnum.TAB_1_13);
         }
     };

     Map<String, String> tempMap = new HashMap(){
         {
             this.put(TAB_1_1, "template_1_1_1.xlsx");
             this.put(TAB_1_2_1, "template_1_2_1.xlsx");
             this.put(TAB_1_6, "template_1_6_1.xlsx");
             this.put(TAB_1_9, "template_1_9_1.xlsx");
             this.put(TAB_1_10, "template_1_10_1.xlsx");
             this.put(TAB_1_1_2, "template_1_1.xlsx");
             this.put(TAB_1_2, "template_1_2.xlsx");
             this.put(TAB_1_3, "template_1_3.xlsx");
             this.put(TAB_1_4, "template_1_4.xlsx");
             this.put(TAB_1_5, "template_1_5.xlsx");
             this.put(TAB_1_6_2, "template_6.xlsx");
             this.put(TAB_1_7, "template_1_7.xlsx");
             this.put(TAB_1_8, "template_1_8.xlsx");
             this.put(TAB_1_9_2, "template_1_9.xlsx");
             this.put(TAB_1_10_2, "template_1_10.xlsx");
             this.put(TAB_1_11, "template_1_11.xlsx");
             this.put(TAB_1_12, "template_1_12.xlsx");
             this.put(TAB_1_13, "template_1_13.xlsx");
         }
     };
}
