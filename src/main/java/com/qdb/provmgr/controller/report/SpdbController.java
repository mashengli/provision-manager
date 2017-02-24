package com.qdb.provmgr.controller.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qdb.provmgr.controller.report.view.spdb.TableStatus;
import com.qdb.provmgr.dao.model.spdb.eum.BankCodeEnum;
import com.qdb.provmgr.dao.model.spdb.eum.TableEnum;
import com.qdb.provmgr.report.spdb.SpdbConstant;
import com.qdb.provmgr.service.spdb.SpdbFtpService;
import com.qdb.provmgr.service.spdb.SpdbReportService;
import com.qdb.provmgr.util.spdb.SpdbDateUtil;
import com.qdb.provmgr.util.spdb.SpdbFileUtil;
import com.qdb.provmgr.util.spdb.SpdbZipUtil;

/**
 * 江苏 浦发银行接口
 * 
 * @author fanjunjian
 *
 */
@Controller
@RequestMapping("/report/{bankName}")
public class SpdbController {

	private static Logger log = LoggerFactory.getLogger(SpdbController.class);

	@Autowired
	private SpdbReportService exportService;
	@Autowired
	private SpdbFtpService spdbFtpService;
	
	/**
	 * 获取各个报表的状态
	 * 
	 * @param bankName
	 * @param start_day
	 * @param end_day
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/list")
	public String tableList(@PathVariable("bankName") String bankName, @RequestParam("start_day") String start_day,
			@RequestParam("end_day") String end_day) {
		String bankNameParam = null;
		Map<String, Object> resultMap = new LinkedHashMap<>();
		if (bankName.equals("bojs")) {
			bankNameParam = BankCodeEnum.JIANGSU.getBankName();
		} else if (bankName.equals("spdb")) {
			bankNameParam = BankCodeEnum.PUFA.getBankName();
		} else {
			resultMap.put("code", "400");
			resultMap.put("message", "无法处理该银行请求");
			return JSON.toJSONString(resultMap);
		}
		Date date = new Date();
		String checkedMonth = SpdbDateUtil.getYYYYMM(start_day);
		String currenMonth = SpdbDateUtil.getYYYYMM(date);
		int diff = 0;
		try {
			diff = SpdbDateUtil.monthShort(checkedMonth, currenMonth);
		} catch (ParseException e1) {
			log.error("计算月差出错");
			resultMap.put("code", "400");
			resultMap.put("message", "查询失败，请确认时间");
			return JSON.toJSONString(resultMap);
		}
		String today = SpdbDateUtil.getYYYYMMDD(new Date());
		Map<String, String> status = null;
		if (diff == 1 || diff == 0) {
			try {
				status = spdbFtpService.listTableStatus(bankNameParam,checkedMonth, today);
			} catch (IOException e) {
				resultMap.put("code", "400");
				resultMap.put("message", "查询失败");
				return JSON.toJSONString(resultMap);
			}

		} else {
			try {
				status = spdbFtpService.listTableStatus(bankNameParam,checkedMonth, "");
			} catch (IOException e) {
				resultMap.put("code", "400");
				resultMap.put("message", "查询失败");
				return JSON.toJSONString(resultMap);
			}

		}
		if (status == null) {
			resultMap.put("code", "400");
			resultMap.put("message", "查询失败");
			return JSON.toJSONString(resultMap);
		}
		List<TableStatus> tableList = new ArrayList<>();
		for (Map.Entry<String, String> entry : status.entrySet()) {
			for (TableEnum table : TableEnum.values()) {
				if (entry.getKey().equals(table.getKey())) {
					TableStatus tableStatus = new TableStatus(table.getProvTable(), entry.getValue());
					tableList.add(tableStatus);
				}
			}
		}
		resultMap.put("code", "200");
		resultMap.put("data", tableList);
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 生成各个报表
	 * @param bankName
	 * @param param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/create")
	public String export(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("bankName") String bankName, 
			@RequestBody(required = false) String param) {

		JSONObject json = JSON.parseObject(param);
		String start_day = json.getString("start_day");
		String end_day = json.getString("end_day");
		String bankNameParam = null;
		Map<String,String> resultMap = new LinkedHashMap<>();
		if (bankName.equals("bojs")) {
			bankNameParam = BankCodeEnum.JIANGSU.getBankName();
		} else if (bankName.equals("spdb")) {
			bankNameParam = BankCodeEnum.PUFA.getBankName();
		} else {
			resultMap.put("code", "400");
			resultMap.put("message", "无法处理该银行请求");
			return JSON.toJSONString(resultMap);
		}
		Date date = new Date();
		String checkedMonth = SpdbDateUtil.getYYYYMM(start_day);
		String currenMonth = SpdbDateUtil.getYYYYMM(date);
		int diff = 0;
		try {
			diff = SpdbDateUtil.monthShort(checkedMonth, currenMonth);
		} catch (ParseException e1) {
			log.error("计算月差出错");
			resultMap.put("code", "400");
			resultMap.put("message", "查询失败，请确认时间");
			return JSON.toJSONString(resultMap);
		}
		List<String> failList = new ArrayList<>();
		if (diff == 0) {
			resultMap.put("code", "400");
			resultMap.put("message", "当月数据不予生成");
			return JSON.toJSONString(resultMap);
		} else{
			String tradeDate = SpdbDateUtil.getYYYYMMDD(date);
			String content = null;
			String fileName_prefix = null;
			JSONObject data = null;
			JSONArray dataArray = json.getJSONArray("report_list");
			for (int i = 0; i < dataArray.size(); i++) {
				data = dataArray.getJSONObject(i);
				String report_name = data.getString("report_name");
				switch (report_name) {
				case SpdbConstant.TABLE_1:
					fileName_prefix = TableEnum.TABLE1.getKey();
					content = exportService.getT1_1Data(tradeDate, bankNameParam, start_day, end_day);
					break;
				case SpdbConstant.TABLE_2:
					fileName_prefix = TableEnum.TABLE2.getKey();
					content = exportService.getT1_2Data(tradeDate, bankNameParam, start_day, end_day);
					break;
				case SpdbConstant.TABLE_3:
					fileName_prefix = TableEnum.TABLE3.getKey();
					content = exportService.getT1_3Data(tradeDate, bankNameParam, start_day, end_day);
					break;
				case SpdbConstant.TABLE_6:
					fileName_prefix = TableEnum.TABLE6.getKey();
					content = exportService.getT1_6Data(tradeDate, bankNameParam, start_day, end_day);
					break;
				case SpdbConstant.TABLE_9:
					fileName_prefix = TableEnum.TABLE9.getKey();
					content = exportService.getT1_9Data(tradeDate, bankNameParam, start_day, end_day);
					break;
				case SpdbConstant.TABLE_10:
					fileName_prefix = TableEnum.TABLE10.getKey();
					content = exportService.getT1_10Data(tradeDate, bankNameParam, start_day, end_day);
					break;
				case SpdbConstant.TABLE_13:
					fileName_prefix = TableEnum.TABLE13.getKey();
					content = exportService.getT1_13Data(tradeDate, bankNameParam, start_day, end_day);
					break;
				case SpdbConstant.TABLE_1_ADD:
					fileName_prefix = TableEnum.TABLE1_ADD.getKey();
					content = exportService.getT1_1ADDData();
					break;
				case SpdbConstant.TABLE_DATE:
					fileName_prefix = TableEnum.TABLEDATE.getKey();
					content = exportService.getTDate_Data(tradeDate, bankNameParam, start_day, end_day);
					break;
				}
				String fileName = fileName_prefix + "_" + tradeDate + "_" + SpdbConstant.ORGANIZATIONID + ".dat";
				String tempPath = spdbFtpService.getSpdbTempPath() + SpdbConstant.SYSTEM_PATH_SEPARATOR + "spdb" + SpdbConstant.SYSTEM_PATH_SEPARATOR;
				File tempPath_ = new File(tempPath);
				if(!tempPath_.exists()|| !tempPath_.isDirectory()){
					tempPath_.mkdirs();
				}
				String tempFile = tempPath + fileName;
				File tempFile_ = new File(tempFile);
				if(tempFile_.exists()){
					tempFile_.delete();
				}
				if (content != null) {
					try {
						SpdbFileUtil.writeToFile(tempFile, content);
					} catch (IOException e) {
						log.error("----------------{}临时文件生成失败------------", fileName);
						failList.add(report_name);
						continue;
					}
					String MonthDir = SpdbDateUtil.getYYYYMM(start_day);
					boolean result = false;
					try {
						result = spdbFtpService.uploadFileToFtp(bankNameParam,tempFile, MonthDir, fileName, fileName_prefix);
					} catch (IOException e) {
						log.error("----------------{}文件上传失败------------", fileName);
						failList.add(report_name);
						continue;
					}
					if (!result) {
						log.error("----------------{}文件上传失败返回false------------", fileName);
						failList.add(report_name);
						continue;
					}
				} else {
					log.error("----------------{}文件没有取到数据------------", fileName);
					failList.add(report_name);
					continue;
				}
				tempFile_.delete();
			}

		} 
		if(failList.size() > 0){
			StringBuffer buffer = new StringBuffer();
			for(int i=0; i<failList.size(); i++){
				buffer.append(failList.get(i) + "  ");
			}
			resultMap.put("code", "400");
			resultMap.put("message", "生成失败:"+buffer.toString());
			return JSON.toJSONString(resultMap);
		}
		resultMap.put("code", "200");
		resultMap.put("message", "生成成功");
		return JSON.toJSONString(resultMap);
	}
	/**
	 * 单个文件下载
	 *
	 */
	@ResponseBody
	@RequestMapping("/download")
	public String downLoadFile(@PathVariable("bankName") String bankName, 
			@RequestParam("start_day") String start_day,
			@RequestParam("end_day") String end_day, 
			@RequestParam("report_name") String report_name,
			HttpServletRequest request, 
			HttpServletResponse response) throws UnsupportedEncodingException {
		String bankNameParam = null;
		Map<String,String> result = new LinkedHashMap<>();
		if (bankName.equals("bojs")) {
			bankNameParam = BankCodeEnum.JIANGSU.getBankName();
		} else if (bankName.equals("spdb")) {
			bankNameParam = BankCodeEnum.PUFA.getBankName();
		} else {
			result.put("code", "400");
			result.put("message", "无法处理该银行请求");
			return JSON.toJSONString(result);
		}
		Date date = new Date();
		String checkedMonth = SpdbDateUtil.getYYYYMM(start_day);
		String currenMonth = SpdbDateUtil.getYYYYMM(date);
		String today = SpdbDateUtil.getYYYYMMDD(new Date());
		int diff = 0;
		try {
			diff = SpdbDateUtil.monthShort(checkedMonth, currenMonth);
		} catch (ParseException e1) {
			log.error("计算月差出错");
			result.put("code", "400");
			result.put("message", "下载失败，请确认时间");
			return JSON.toJSONString(result);
		}

		FTPClient ftp = spdbFtpService.getFtpConnection();
		String fileName = null;
		if (diff == 0) {
			result.put("code", "400");
			result.put("message", "当月无法下载");
			return JSON.toJSONString(result);
		} else if (diff == 1) {
			fileName = spdbFtpService.getFileName(bankNameParam,ftp, checkedMonth, report_name, today);
		} else {
			fileName = spdbFtpService.getFileName(bankNameParam,ftp, checkedMonth, report_name, "");
		}
		if (fileName == null) {
			spdbFtpService.close(ftp);
			result.put("code", "400");
			result.put("message", "文件不存在");
			return JSON.toJSONString(result);
		}
		InputStream in = null;
		try {
			in = spdbFtpService.downLoad(bankNameParam,checkedMonth, fileName);
		} catch (Exception e) {
			result.put("code", "400");
			result.put("message", e.getMessage());
			return JSON.toJSONString(result);
		}
		if (in == null) {
			spdbFtpService.close(ftp);
			result.put("code", "400");
			result.put("message", "文件下载失败");
			return JSON.toJSONString(result);
		}
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String(fileName.getBytes("iso8859-1"), "UTF-8"));
		OutputStream out = null;
		try {
			out = response.getOutputStream();
		} catch (IOException e) {
			log.error("下载{}文件时获取输出流失败", fileName);
			result.put("code", "400");
			result.put("message", "文件下载失败");
			return JSON.toJSONString(result);
		}
		byte buffer[] = new byte[1024];
		int len = 0;
		try {
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (IOException e) {
			log.error("下载{}文件时写入失败", fileName);
			result.put("code", "400");
			result.put("message", "文件下载失败");
			return JSON.toJSONString(result);
		} finally {
			spdbFtpService.close(ftp);
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
				
			} catch (IOException e) {
				log.error("下载{}文件时写入失败", fileName);
			}

		}
		result.put("code", "200");
		result.put("message", "文件下载成功");
		return JSON.toJSONString(result);
	}
	
	/**
	 * 下载全部
	 */
	@ResponseBody
	@RequestMapping("/download-all")
	public String downLoadAll(@PathVariable("bankName") String bankName, 
			@RequestParam("start_day") String start_day,
			@RequestParam("end_day") String end_day,
			HttpServletRequest request, 
			HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String,String> result = new LinkedHashMap<>();
		String bankNameParam = null;
		if (bankName.equals("bojs")) {
			bankNameParam = BankCodeEnum.JIANGSU.getBankName();
		} else if (bankName.equals("spdb")) {
			bankNameParam = BankCodeEnum.PUFA.getBankName();
		} else {
			result.put("code", "400");
			result.put("message", "无法处理该银行请求");
			return JSON.toJSONString(result);
		}
		String checkedMonth = SpdbDateUtil.getYYYYMM(start_day);
		String tempDir = checkedMonth + "_" + UUID.randomUUID().toString();
		String tempPath = spdbFtpService.getSpdbTempPath() + SpdbConstant.SYSTEM_PATH_SEPARATOR + "spdb" + SpdbConstant.SYSTEM_PATH_SEPARATOR + tempDir;
		File file = new File(tempPath);
		if(!file.exists() || !file.isDirectory()){
			boolean make = file.mkdirs();
			if(!make){
				result.put("code", "400");
				result.put("message", "文件下载失败，获取远程文件出错");
				return JSON.toJSONString(result);
			}
		}
		boolean getSuccess = spdbFtpService.getFtpFileToLocal(bankNameParam,checkedMonth, tempPath + SpdbConstant.SYSTEM_PATH_SEPARATOR);
		if(!getSuccess){
			result.put("code", "400");
			result.put("message", "文件下载失败，请确认是否全部生成");
			return JSON.toJSONString(result);
		}
		String fileName = tempDir + ".zip";
		SpdbZipUtil.compress(tempPath,  spdbFtpService.getSpdbTempPath() + SpdbConstant.SYSTEM_PATH_SEPARATOR + "spdb" + SpdbConstant.SYSTEM_PATH_SEPARATOR + fileName);
		
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String(fileName.getBytes("iso8859-1"), "UTF-8"));
		OutputStream out = null;
		InputStream in = null;
		try {
			out = response.getOutputStream();
			in = new FileInputStream(new File(spdbFtpService.getSpdbTempPath() + SpdbConstant.SYSTEM_PATH_SEPARATOR + "spdb" + SpdbConstant.SYSTEM_PATH_SEPARATOR + fileName));
		} catch (IOException e) {
			log.error("下载{}文件时获取输出流失败", fileName);
			result.put("code", "400");
			result.put("message", "文件下载失败");
			return JSON.toJSONString(result);
		}
		byte buffer[] = new byte[1024];
		int len = 0;
		try {
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (IOException e) {
			log.error("下载{}文件时写入失败", fileName);
			result.put("code", "400");
			result.put("message", "文件下载失败");
			return JSON.toJSONString(result);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
				
			} catch (IOException e) {
				log.error("下载{}文件时写入失败", fileName);
			}
		}	
		result.put("code", "200");
		result.put("message", "文件下载成功");
		return JSON.toJSONString(result);
	}

}
