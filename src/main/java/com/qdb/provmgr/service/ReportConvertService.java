package com.qdb.provmgr.service;

import com.alibaba.druid.sql.visitor.functions.If;
import com.qdb.provmgr.inter.FileListFilter;
import com.qdb.provmgr.report.ccb.ExcelUtils;
import com.qdb.provmgr.util.constant.NewPbcConvertDestExcelNameEnum;
import com.qdb.provmgr.util.constant.ReportConvertConstant;
import com.qdb.provmgr.util.constant.ReportConvertSourcesEnum;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenzhong on 2017/2/17.
 */
@Service
public class ReportConvertService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<String, ReportConvertSourcesEnum> srcMap = ReportConvertConstant.srcMap;
    private final Map<String, NewPbcConvertDestExcelNameEnum> destNameMap = ReportConvertConstant.destNameMap;
    private final Map<String, String> tempMap = ReportConvertConstant.tempMap;

    private final String ACCOUNT_REG = "^\\d{6}$";


    private final String ACCOUT_NO = "account";
    private final String DATA_LIST = "data";
    private final String DATA_KEY = "data_list";
    private final String TEMP_PARENT_PATH = "excelTemplate/tonewpbc";

    /**
     * 将文件按照表名进行分类
     * @param file
     */
    public void fileGrouping(File file) {
        String fileName = file.getName();

        try {
            String tabNo = this.parseTabNo(file);
            int length = fileName.length();
            String substr = fileName.substring(length - 10, length - 4);
            boolean isAccount = substr.matches(ACCOUNT_REG);

            String monthStr = fileName.substring(0, 6);
            String srcPath = file.getAbsolutePath();
            StringBuilder destDir = new StringBuilder(ReportConvertConstant.ROOT_DIR + monthStr + "/");
            if (!isAccount){
                tabNo = "total";
            }

            destDir.append(tabNo + "/");
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

    /**
     * 生成文件
     * @param request
     * @param rootDir
     */
    public void createFiles(HttpServletRequest request, String rootDir) {
        File rootFiles = new File(rootDir);
        File[] monthDirs = rootFiles.listFiles(new FileListFilter()); //各个月份
        for (File monthDir : monthDirs) {
            String monthDirName = monthDir.getName();

            File[] tabDirs = monthDir.listFiles(new FileListFilter()); //各个表标识目录
            if (tabDirs == null || tabDirs.length <= 0) {
                return;
            }
            for (File tabFile : tabDirs) {
                String tabNo = tabFile.getName();

                if ("1_3".equals(tabNo) || "1_13".equals(tabNo)) {
                    continue;
                }

                File[] realExcels = tabFile.listFiles(new FileListFilter()); //各个excel文件
                //汇总表
                if ("total".equals(tabNo)) {
                    try {
                        for (File excelFile : realExcels) {
                            List<List<Map<String, Object>>> dataBodyList = new ArrayList<>();
                            Map<String, Object> excelDataMap = this.buildExcelDataMap(excelFile);
                            List<Map<String, Object>> dataList = (List<Map<String, Object>>) excelDataMap.get(DATA_LIST);
                            List<String> sheetNameList = new ArrayList<>();
                            dataBodyList.add(dataList);
                            sheetNameList.add("COUNT");

                            String tabFlag = this.parseTabNo(excelFile);
                            String tempName = tempMap.get(tabFlag);
                            String tempPath = TEMP_PARENT_PATH + "/" + tempName;

                            NewPbcConvertDestExcelNameEnum totalEnum = destNameMap.get(tabFlag);
                            String destName = totalEnum.getText();
                            String filePath = ReportConvertConstant.FINAL_PATH + monthDirName;
                            File file = new File(filePath);
                            if(file.exists()){
                                file.delete();
                            }
                            file.mkdirs();

                            String destPath =  filePath + "/" + destName;
                            this.createExcel(request, dataBodyList, sheetNameList, tempPath, destPath, null);
                        }
                    } catch (InvalidFormatException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //账户表
                    NewPbcConvertDestExcelNameEnum accountTabEnum = destNameMap.get(tabNo);
                    String destAccountFileName = accountTabEnum.getText();
                    String tempName = tempMap.get(tabNo);
                    String tempPath = TEMP_PARENT_PATH + "/" + tempName;

                    String filePath = ReportConvertConstant.FINAL_PATH+ "/" + monthDirName + "/";
                    File file = new File(filePath);
                    if(file.exists()){
                        file.delete();
                    }
                    file.mkdirs();
                    String destPath =  filePath + "/" + destAccountFileName;

                    List<String> sheetNameList = new ArrayList<>();
                    List<List<Map<String, Object>>> dataBodyList = new ArrayList<>();

                    try {
                        for (File curFile: realExcels) {
                            Map<String, Object> excelDataMap = this.buildExcelDataMap(curFile);
                            List<Map<String, Object>> dataList = (List<Map<String, Object>>) excelDataMap.get(DATA_LIST);
                            String accountNo = (String) excelDataMap.get(ACCOUT_NO);
                            dataBodyList.add(dataList);
                            sheetNameList.add(accountNo);
                        }
                        this.createExcel(request, dataBodyList, sheetNameList, tempPath, destPath, null);
                    } catch (InvalidFormatException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 生成文件
     * @param request
     * @param dataList
     * @param sheetNameList
     * @param tempRelativePath
     * @param destExcelPath
     * @param map
     * @throws InvalidFormatException
     * @throws IOException
     */
    private void createExcel(HttpServletRequest request, List dataList, List<String> sheetNameList, String tempRelativePath,
                             String destExcelPath, Map<String, Object> map) throws InvalidFormatException, IOException {
        InputStream is = null;
        OutputStream os = null;
        String excelTemplatePath = request.getServletContext().getRealPath("/WEB-INF/" + tempRelativePath);

        XLSTransformer transformer = new XLSTransformer();
        Workbook workbook;

        try {
            File tempFile = new File(excelTemplatePath);
            File destFile = new File(destExcelPath);
            if (destFile.exists()) {
                destFile.delete();
            }

            is = new FileInputStream(tempFile);
            os = new FileOutputStream(destFile);
            workbook = transformer.transformMultipleSheetsList(is, dataList, sheetNameList, DATA_KEY, map, 0);
            workbook.write(os);
            // 将写入到客户端的内存的数据刷新到磁盘
            os.flush();
        } catch (InvalidFormatException e) {
            logger.error("**********生成报表{}出现异常, 异常信息:{}", destExcelPath, e.getMessage());
            throw e;
        } catch (IOException e) {
            logger.error("**********生成报表{}出现异常, 异常信息:{}", destExcelPath, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("**********生成报表{}出现异常, 异常信息:{}", destExcelPath, e.getMessage());
            throw e;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    private Map<String, Object> buildExcelDataMap(File file) {
        String tabNo = this.parseTabNo(file);
        ReportConvertSourcesEnum srcEnum = srcMap.get(tabNo);

        int actCol = srcEnum.getActCol();
        int actRow = srcEnum.getActRow();

        int startCol = srcEnum.getStartCol();
        int startRow = srcEnum.getStartRow();
        int endCol = srcEnum.getEndCol();
        int endRow = srcEnum.getEndRow();
        Map<String, Object> resultMap;

        if ("1_6".equals(tabNo) || "1_6_2".equals(tabNo) || "1_9".equals(tabNo) || "1_9_2".equals(tabNo)
                || "1_11".equals(tabNo) || "1_12".equals(tabNo)) {
            resultMap = this.findConvertData(file, actCol, actRow, startCol, endCol, startRow, endRow);
        } else {
            resultMap = this.findNormalExcelData(file, actCol, actRow, startCol, endCol, startRow, endRow);
        }
        return resultMap;
    }

    /**
     * 从文件名中解析表标识
     *
     * @param file
     * @return
     */
    private String parseTabNo(File file) {
        String fileName = file.getName();
        int startIndex = fileName.indexOf("表");
        int endIndex = fileName.indexOf("[");

        //获取表标识，如1_1
        String tabNo = fileName.substring(startIndex + 1, endIndex);
        return tabNo;
    }

    //获取需要转置的excel数据
    private Map<String, Object> findConvertData(File file, int actCol, int actRow, int startCol, int endCol, int startRow, int endRow) {
        Map<String, Object> resultMap = new HashMap<>(2);
        List<Map<String, Object>> dataList = new ArrayList<>(50);
        try {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);

            String accountNo = this.findAccountNo(sheet, actCol, actRow);
            resultMap.put(ACCOUT_NO, accountNo);

            int col = startCol + 1;
            while (col <= endCol) {
                Map<String, Object> colMap = new HashMap(50);
                int r = startRow;
                while (r <= endRow) {
                    Row row = sheet.getRow(r);
                    Cell headCell = row.getCell(startCol);
                    String keyStr = headCell.getStringCellValue();
                    this.buildDataMap(sheet, keyStr, r, col, colMap);
                    r++;
                }
                dataList.add(colMap);
                col++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        resultMap.put(DATA_LIST, dataList);
        return resultMap;
    }

    /**
     * 获取账户表账户号，汇总表置空
     *
     * @param sheet
     * @param actCol
     * @param actRow
     * @return
     */
    private String findAccountNo(Sheet sheet, int actCol, int actRow) {
        if (actRow < 0) {
            return null;
        }
        Row row = sheet.getRow(actRow);
        Cell rowCell = row.getCell(actCol);
        return rowCell.getStringCellValue();
    }

    /**
     * 获取不用转置的excel数据
     *
     * @param file
     * @param actCol
     * @param actRow
     * @param startCol
     * @param endCol
     * @param startRow
     * @param endRow
     * @return
     */
    private Map<String, Object> findNormalExcelData(File file, int actCol, int actRow, int startCol, int endCol, int startRow, int endRow) {
        Map<String, Object> resultMap = new HashMap<>(2);
        List<Map<String, Object>> dataList = new ArrayList<>(50);
        try {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);
            String accountNo = this.findAccountNo(sheet, actCol, actRow);
            resultMap.put(ACCOUT_NO, accountNo);

            String tabNo = this.parseTabNo(file);

            Row headRow = sheet.getRow(startRow);
            int row = startRow + 1;
            while (row <= endRow) {
                int col = startCol;
                Map<String, Object> rowMap = new HashMap(50);

                String headKey = "DATA";
                while (col <= endCol) {
                    String keyStr;
                    if ("1_3".equals(tabNo)){
                        keyStr = headKey + (col + 1);
                        headKey += 1;
                    }else{
                        Cell headCell = headRow.getCell(col);
                        keyStr = headCell.getStringCellValue();
                    }
                    this.buildDataMap(sheet, keyStr, row, col, rowMap);
                    col++;
                }
                //每行的map放入list
                dataList.add(rowMap);
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        resultMap.put(DATA_LIST, dataList);
        return resultMap;
    }

    /**
     * 将单元格中的数据放在map中
     *
     * @param sheet
     * @param keyStr
     * @param r
     * @param col
     * @param cellMap
     */
    private void buildDataMap(Sheet sheet, String keyStr, int r, int col, Map<String, Object> cellMap) {
        Row curRow = sheet.getRow(r);
        Cell cell = curRow.getCell(col);
        int cellType = cell.getCellType();
        Object cellValue = null;
        switch (cellType) {
            case Cell.CELL_TYPE_NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;
            default:
                break;
        }
        cellMap.put(keyStr, cellValue);
    }


}
