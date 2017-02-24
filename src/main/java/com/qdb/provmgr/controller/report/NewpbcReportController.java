package com.qdb.provmgr.controller.report;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.qdb.provmgr.dao.TableModeEnum;
import com.qdb.provmgr.dao.entity.report.BaseReportEntity;
import com.qdb.provmgr.dao.entity.report.DataTable1_1;
import com.qdb.provmgr.dao.entity.report.DataTable1_10;
import com.qdb.provmgr.dao.entity.report.DataTable1_11;
import com.qdb.provmgr.dao.entity.report.DataTable1_12;
import com.qdb.provmgr.dao.entity.report.DataTable1_13;
import com.qdb.provmgr.dao.entity.report.DataTable1_2;
import com.qdb.provmgr.dao.entity.report.DataTable1_3;
import com.qdb.provmgr.dao.entity.report.DataTable1_4;
import com.qdb.provmgr.dao.entity.report.DataTable1_5;
import com.qdb.provmgr.dao.entity.report.DataTable1_6;
import com.qdb.provmgr.dao.entity.report.DataTable1_7;
import com.qdb.provmgr.dao.entity.report.DataTable1_8;
import com.qdb.provmgr.dao.entity.report.DataTable1_9;
import com.qdb.provmgr.report.PresetContent;
import com.qdb.provmgr.report.ReportHelper;
import com.qdb.provmgr.report.newpbc.NewpbcExcelUtil;
import com.qdb.provmgr.report.newpbc.NewpbcReportHelper;
import com.qdb.provmgr.service.FtpFileService;
import com.qdb.provmgr.service.pbc.PbcReportService;
import com.qdb.provmgr.util.DateUtils;
import com.qdb.provmgr.util.HttpRequestUtil;

/**
 * @author mashengli
 */
@Controller
@RequestMapping(value = "/report/newpbc")
public class NewpbcReportController {

    private Logger log = LoggerFactory.getLogger(NewpbcReportController.class);

    @Autowired
    private PbcReportService pbcReportService;
    @Autowired
    private FtpFileService ftpFileService;
    @Autowired
    private NewpbcReportHelper newpbcReportHelper;
    @Autowired
    private ReportHelper reportHelper;

    /**
     * 获取报表列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Map index(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<>();
        String startDateStr = request.getParameter("start_day");
        String endDateStr = request.getParameter("end_day");
        String reportType = request.getParameter("report_type");
        if (StringUtils.isBlank(startDateStr)) {
            resultMap.put("code", 400);
            resultMap.put("message", "日期不能为空");
            return resultMap;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(DateUtils.getFirstDayOfMonth(sdf.parse(startDateStr)));
            endDate = sdf.parse(DateUtils.getLastDayOfMonth(sdf.parse(endDateStr)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String remoteDir = newpbcReportHelper.getPbcFtpDir(new SimpleDateFormat("yyyyMM").format(startDate));
        long count = ftpFileService.countFiles(remoteDir, newpbcReportHelper.FILE_SUFFIX);
        resultMap.put("file_count", count);
        resultMap.put("reportType", reportType);
            //汇总类型的报表，不再区分银行和账户，只区分哪张表
        getReportList(resultMap, startDate, endDate);
        return resultMap;
    }

    /**
     * 生成报表接口
     *
     * @return
     */
    @RequestMapping(value = "create")
    @ResponseBody
    public Map createReport(HttpServletRequest request, @RequestBody String jsonData) {
        Map<String, Object> resultMap = new HashMap<>();
        String charset = StringUtils.isNotEmpty(request.getCharacterEncoding()) ? request.getCharacterEncoding() : "ISO-8859-1";
        JSONObject jsonObject = null;
        String reportType = null;
        Date startDate = null;
        Date endDate = null;
        List<Map<String, Object>> reportListParam = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            jsonObject = JSONObject.parseObject(new String(jsonData.getBytes(charset), "UTF-8"));
            reportType = String.valueOf(jsonObject.get("report_type"));
            startDate = sdf.parse(DateUtils.getFirstDayOfMonth(sdf.parse((String) jsonObject.get("start_day"))));
            endDate = sdf.parse(DateUtils.getLastDayOfMonth(sdf.parse((String) jsonObject.get("end_day"))));
            reportListParam = (List<Map<String, Object>>) jsonObject.get("report_list");
        } catch (Exception e) {
            resultMap.put("code", 400);
            resultMap.put("message", "数据格式有误");
            return resultMap;
        }
        if (StringUtils.isBlank(reportType) || CollectionUtils.isEmpty(reportListParam)) {
            resultMap.put("code", 400);
            resultMap.put("message", "参数不全");
            return resultMap;
        }
        int success = 0;
        int total = 0;
        PresetContent presetContent = new PresetContent();
        presetContent.setReportType(reportType);
        for (Map<String, Object> map : reportListParam) {
            total++;
            TableModeEnum tableMode = TableModeEnum.getEnumByTableName(String.valueOf(map.get("report_name")));
            String dir = newpbcReportHelper.getPbcFtpDir(new SimpleDateFormat("yyyyMM").format(startDate));
            String fileName = newpbcReportHelper.getPbcFileName(tableMode);
            boolean uploadResult = false;
            try {
                File tempFile = NewpbcExcelUtil.createExcelFile(tableMode, request.getServletContext().getRealPath
                                ("/WEB-INF/" + newpbcReportHelper.getRelativePbcTemplateFile(tableMode)),
                        fileName, presetContent, getDataList(tableMode, startDate, endDate));
                uploadResult = ftpFileService.uploadFileToFtp(tempFile.getAbsolutePath(), dir + fileName);
                if (uploadResult) {
                    success++;
                }
            } catch (Exception e) {
                log.error("生成报表异常,将尝试删除ftp已有报表文件", e);
            }
            if (!uploadResult) {
                log.info("生成报表失败,正在删除ftp已有报表文件");
                ftpFileService.deleteFile(dir + fileName);
            }
        }
        if (success < total) {
            resultMap.put("code", 400);
            resultMap.put("message", "共记" + total + "张报表，创建成功" + success + "张，创建失败" + (total - success) + "张");
        } else {
            resultMap.put("code", 200);
            resultMap.put("message", "成功生成" + total + "张报表");
        }
        return resultMap;
    }

    @RequestMapping(value = "download")
    @ResponseBody
    public Map download(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<>();
        String charset = StringUtils.isNotEmpty(request.getCharacterEncoding()) ? request.getCharacterEncoding() : "ISO-8859-1";
        String startDateStr = request.getParameter("start_day");
        String reportType = request.getParameter("report_type");
        String reportName = request.getParameter("report_name");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isBlank(startDateStr) || StringUtils.isBlank(reportType) || StringUtils.isBlank(reportName)) {
            resultMap.put("code", 400);
            resultMap.put("message", "参数不全");
            return resultMap;
        }
        try {
            Date startDate = sdf.parse(DateUtils.getFirstDayOfMonth(sdf.parse(startDateStr)));
            Date endDate = sdf.parse(DateUtils.getLastDayOfMonth(sdf.parse(startDateStr)));
            TableModeEnum tableModeEnum = TableModeEnum.getEnumByTableName(new String(reportName.getBytes(charset), "UTF-8"));
            if (tableModeEnum == null) {
                resultMap.put("code", 400);
                resultMap.put("message", "请选择报表");
                return resultMap;
            }
            String ftpPath = null;
            String fileName = null;
            ftpPath = newpbcReportHelper.getPbcFtpDir(new SimpleDateFormat("yyyyMM").format(startDate));
            fileName = newpbcReportHelper.getPbcFileName(tableModeEnum);
            if (StringUtils.isBlank(fileName) || !ftpFileService.isFileExists(ftpPath, fileName)) {
                resultMap.put("code", 400);
                resultMap.put("message", "文件不存在");
                return resultMap;
            }
            // 设置response
            response.reset();
            response.setHeader("Cache-Control", "private");
            response.setHeader("Pragma", "private");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Type", "application/force-download");
            response.setHeader("Content-Disposition", "attachment;filename=" + HttpRequestUtil.getAttachFileName(request, fileName));
            ftpFileService.downloadFileFromFtp(ftpPath + fileName, response);
            resultMap.put("code", 200);
            resultMap.put("message", "SUCCESS");
            return resultMap;
        } catch (Exception e) {
            log.error("下载异常", e);
        }
        resultMap.put("code", 400);
        resultMap.put("message", "下载失败");
        return resultMap;
    }

    @RequestMapping(value = "download-all")
    @ResponseBody
    public Map downloadAll(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<>();
        String startDateStr = request.getParameter("start_day");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isBlank(startDateStr)) {
            resultMap.put("code", 400);
            resultMap.put("message", "日期不能为空");
            return resultMap;
        }
        try {
            Date startDate = sdf.parse(DateUtils.getFirstDayOfMonth(sdf.parse(startDateStr)));
            Date endDate = sdf.parse(DateUtils.getLastDayOfMonth(sdf.parse(startDateStr)));
            String ftpPath = newpbcReportHelper.getPbcFtpDir(new SimpleDateFormat("yyyyMM").format(startDate));
            String fileName = newpbcReportHelper.getPbcZipFileName(startDate, endDate, reportHelper.getCompanyName());
            ftpFileService.downloadAndCompressFromFtp(request, response, ftpPath, fileName, newpbcReportHelper.FILE_SUFFIX);
        } catch (Exception e) {
            log.error("下载异常", e);
        }
        resultMap.put("code", 400);
        resultMap.put("message", "下载失败");
        return resultMap;
    }

    private void getReportList(Map<String, Object> resultMap, Date startDate, Date endDate) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<TableModeEnum> tables;
        if (null == resultMap.get("reportType")) {
            tables = newpbcReportHelper.getPbcReportTablesAll();
        } else if ("0".equals(resultMap.get("reportType").toString())){
            tables = newpbcReportHelper.getPbcReportTablesTotal();
        } else {
            tables = newpbcReportHelper.getPbcReportTablesAccount();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        for (TableModeEnum tableMode : tables) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("bank_name", "中国人民银行");
            dataMap.put("report_name", tableMode.getTableName());
            boolean status = ftpFileService.isFileExists(newpbcReportHelper.getPbcFtpDir(sdf.format(startDate)),
                    newpbcReportHelper.getPbcFileName(tableMode));
            dataMap.put("report_status", status ? "1" : "0");
            dataList.add(dataMap);
        }

        resultMap.put("code", 200);
        resultMap.put("data", dataList);
    }

    private List<BaseReportEntity> getDataList(TableModeEnum tableMode, Date startDate, Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<BaseReportEntity> resultList = new ArrayList<>();
        if (TableModeEnum.Table1_1.equals(tableMode) || TableModeEnum.Table1_1_1.equals(tableMode)) {
            List<DataTable1_1> list = pbcReportService.queryForList(tableMode, sdf.format(startDate),
                    sdf.format(endDate), null);
            resultList.addAll(list);
        }
        if (TableModeEnum.Table1_2.equals(tableMode) || TableModeEnum.Table1_2_1.equals(tableMode)) {
            List<DataTable1_2> list = pbcReportService.queryForList(tableMode, sdf.format(startDate),
                    sdf.format(endDate), null);
            resultList.addAll(list);
        }
        if (TableModeEnum.Table1_3.equals(tableMode)) {
            List<DataTable1_3> list = pbcReportService.queryForList(tableMode, sdf.format(startDate),
                    sdf.format(endDate), null);
            resultList.addAll(list);
        }
        if (TableModeEnum.Table1_4.equals(tableMode)) {
            List<DataTable1_4> list = pbcReportService.queryForList(tableMode, sdf.format(startDate),
                    sdf.format(endDate), null);
            resultList.addAll(list);
        }
        if (TableModeEnum.Table1_5.equals(tableMode)) {
            List<DataTable1_5> list = pbcReportService.queryForList(tableMode, sdf.format(startDate),
                    sdf.format(endDate), null);
            resultList.addAll(list);
        }
        if (TableModeEnum.Table1_6.equals(tableMode) || TableModeEnum.Table1_6_1.equals(tableMode)) {
            List<DataTable1_6> list = pbcReportService.queryForList(tableMode, sdf.format(startDate),
                    sdf.format(endDate), null);
            resultList.addAll(list);
        }
        if (TableModeEnum.Table1_7.equals(tableMode)) {
            List<DataTable1_7> list = pbcReportService.queryForList(tableMode, sdf.format(startDate),
                    sdf.format(endDate), null);
            resultList.addAll(list);
        }
        if (TableModeEnum.Table1_8.equals(tableMode)) {
            List<DataTable1_8> list = pbcReportService.queryForList(tableMode, sdf.format(startDate),
                    sdf.format(endDate), null);
            resultList.addAll(list);
        }
        if (TableModeEnum.Table1_9.equals(tableMode) || TableModeEnum.Table1_9_1.equals(tableMode)) {
            List<DataTable1_9> list = pbcReportService.queryForList(tableMode, sdf.format(startDate),
                    sdf.format(endDate), null);
            resultList.addAll(list);
        }
        if (TableModeEnum.Table1_10.equals(tableMode) || TableModeEnum.Table1_10_1.equals(tableMode)) {
            List<DataTable1_10> list = pbcReportService.queryForList(tableMode, sdf.format(startDate),
                    sdf.format(endDate), null);
            resultList.addAll(list);
        }
        if (TableModeEnum.Table1_11.equals(tableMode)) {
            List<DataTable1_11> list = pbcReportService.queryForList(tableMode, sdf.format(startDate),
                    sdf.format(endDate), null);
            resultList.addAll(list);
        }
        if (TableModeEnum.Table1_12.equals(tableMode)) {
            List<DataTable1_12> list = pbcReportService.queryForList(tableMode, sdf.format(startDate),
                    sdf.format(endDate), null);
            resultList.addAll(list);
        }
        if (TableModeEnum.Table1_13.equals(tableMode)) {
            List<DataTable1_13> list = pbcReportService.queryForList(tableMode, sdf.format(startDate),
                    sdf.format(endDate), null);
            resultList.addAll(list);
        }
        return resultList;
    }
}
