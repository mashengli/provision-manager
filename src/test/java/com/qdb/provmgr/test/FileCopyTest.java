package com.qdb.provmgr.test;

import com.qdb.provmgr.report.ccb.ExcelUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by wenzhong on 2017/2/17.
 */
public class FileCopyTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String ACCOUNT_REG = "^\\d{6}$";

    private final String ROOT_DIR = "/Users/wenzhong/report/";
    private final String finalPath = "/Users/wenzhong/dest/";

    @Test
    public void testCopy(){
        String src = "/Users/wenzhong/Downloads/20170101_20170131[BJ0000004]北京钱袋宝支付技术有限公司/表1-12支付机构客户资金账户余额试算表.xlsx";
        String dest =  "/Users/wenzhong/Downloads/20170101_20170131[BJ0000004]北京钱袋宝支付技术有限公司/表1-12支付机构客户资金账户余额试算表2.xlsx";
        ExcelUtils.copyFile(src, dest);
    }

    @Test
    public void testString(){
        String fileName = "20150501_20150531表1_6_2[BJ0000004]北京钱袋宝支付技术有限公司.xls";
        int startIndex = fileName.indexOf("表");
        int endIndex = fileName.indexOf("[");
        String tabFlag = fileName.substring(startIndex+1, endIndex);
        String substring = fileName.substring(fileName.length()-10, fileName.length() - 4);
        boolean matches = fileName.substring(fileName.length()-10, fileName.length() - 4).matches("^[1-9]\\d*$");
        System.out.println(tabFlag+"---" +substring+ "---" + matches);
    }

    @Test
    public void reportConvert(){

        /*
            1、遍历源目录下所有文件
            2、分别将汇总表和账户表分类
            3、读取汇总表和账户数据封装List
            4、调用生成报表接口
         */

        String rootPath = "/Users/wenzhong/Documents/5月/20150501_20150531_备付金_[BJ0000004]北京钱袋宝支付技术有限公司";
        File rootFile = new File(rootPath);
        this.findFiles(rootFile);

    }

    /**
     * 从文件根目录遍历，将文件按名称分类放在同一目录下
     * @param rootFile
     */
    private void findFiles(File rootFile) {
        File[] files = rootFile.listFiles();
        for (File file: files) {
            if (file.isHidden()){
                continue;
            }
            if(file.isDirectory()){
                this.findFiles(file);
            }else{
                this.filesClassify(file);
            }
        }

    }

    private void filesClassify(File file) {
        String fileName = file.getName();

        try {
            //判断是账户表还是汇总表
            int startIndex = fileName.indexOf("表");
            int endIndex = fileName.indexOf("[");

            //获取表标识，如1_1
            String targetDirName = fileName.substring(startIndex+1, endIndex);
            int length = fileName.length();
            String substr = fileName.substring(length - 10, length - 4);
            boolean isAccount = substr.matches(ACCOUNT_REG);

            String monthStr = fileName.substring(0, 6);
            String srcPath = file.getAbsolutePath();
            StringBuilder destDir = new StringBuilder(ROOT_DIR + monthStr + "/");
            if (!isAccount){
                targetDirName = "total";
            }
            destDir.append(targetDirName + "/" );

            File destFile = new File(destDir.toString());
            if (destFile.exists()){
                destFile.delete();
            }

            destFile.mkdirs();
            destDir.append(fileName);

            ExcelUtils.copyFile(srcPath, destDir.toString());
        } catch (Exception e) {
            logger.error("文件分类到指定目录出现异常，文件名称:{}，异常信息：\r\n{}", fileName, e);
        }
    }
}
