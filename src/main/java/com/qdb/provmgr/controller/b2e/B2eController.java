package com.qdb.provmgr.controller.b2e;

import com.alibaba.fastjson.JSONObject;
import com.qdb.provmgr.service.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 银企直连银行明细接口
 * Created by hezhongchen on 2016/12/14.
 */
@RequestMapping("/b2e/bank")
@Controller
public class B2eController {
    private Logger logger = LoggerFactory.getLogger(B2eController.class);

    @Autowired
    private HttpService httpService;

    @Value("${b2e.host}")
    private String b2eHost;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String listBank(){
        String url = b2eHost+"/api/service?flag=0";

        String jsonStr;
        try {
            jsonStr = httpService.httpGet(url);
        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(httpService.RESPONSE_STATUS_CODE_KEY, httpService.RESPONSE_STATUS_CODE_ERROR);
            resultMap.put(httpService.RESPONSE_STATUS_MESSAGE_KEY, e.getMessage());

            jsonStr = JSONObject.toJSONString(resultMap);
            logger.error(e.getMessage());
        }

        return jsonStr;
    }

    @ResponseBody
    @RequestMapping(value = "/account/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String listBankAccount(String bank_type){
        String url = b2eHost+"/api/bank/accouts?bank_type="+bank_type;

        String jsonStr;
        try {
            jsonStr = httpService.httpGet(url);
        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(httpService.RESPONSE_STATUS_CODE_KEY, httpService.RESPONSE_STATUS_CODE_ERROR);
            resultMap.put(httpService.RESPONSE_STATUS_MESSAGE_KEY, e.getMessage());

            jsonStr = JSONObject.toJSONString(resultMap);
            logger.error(e.getMessage());
        }

        return jsonStr;
    }

    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String listBankDetail(String bank_type, String begindate, String enddate, String accno, String currentpage, String pagesize){
        String url = b2eHost+"/api/bank/details?";

        url += (bank_type!=null ? "bank_type="+bank_type : "")
                + (begindate!=null ? "&begindate="+begindate : "")
                + (enddate!=null ? "&enddate="+enddate : "")
                + (accno!=null ? "&accno="+accno : "")
                + (currentpage!=null ? "&currentpage="+currentpage : "")
                + (pagesize!=null ? "&pagesize="+pagesize : "");

        String jsonStr;
        try {
            jsonStr = httpService.httpGet(url);
        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(httpService.RESPONSE_STATUS_CODE_KEY, httpService.RESPONSE_STATUS_CODE_ERROR);
            resultMap.put(httpService.RESPONSE_STATUS_MESSAGE_KEY, e.getMessage());

            jsonStr = JSONObject.toJSONString(resultMap);
            logger.error(e.getMessage());
        }

        return jsonStr;
    }

    @ResponseBody
    @RequestMapping(value = "/balance", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String getBankBalance(String bank_type, String begindate, String enddate, String accno, String currentpage, String pagesize){
        String url = b2eHost+"/api/bank/balances?";

        url += (bank_type!=null ? "bank_type="+bank_type : "")
                + (begindate!=null ? "&begindate="+begindate : "")
                + (enddate!=null ? "&enddate="+enddate : "")
                + (accno!=null ? "&accno="+accno : "")
                + (currentpage!=null ? "&currentpage="+currentpage : "")
                + (pagesize!=null ? "&pagesize="+pagesize : "");

        String jsonStr;
        try {
            jsonStr = httpService.httpGet(url);
        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(httpService.RESPONSE_STATUS_CODE_KEY, httpService.RESPONSE_STATUS_CODE_ERROR);
            resultMap.put(httpService.RESPONSE_STATUS_MESSAGE_KEY, e.getMessage());

            jsonStr = JSONObject.toJSONString(resultMap);
            logger.error(e.getMessage());
        }

        return jsonStr;
    }

    @ResponseBody
    @RequestMapping(value = "/detail/import", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String importBankDetail(@RequestBody String requestJson){
        String url = b2eHost+"/api/bank/details/remote?";

        JSONObject jsonObject = JSONObject.parseObject(requestJson);
        String bank_type = (String) jsonObject.get("bank_type");
        String begindate = (String) jsonObject.get("begindate");
        String enddate = (String) jsonObject.get("enddate");
        String accno = (String) jsonObject.get("accno");

        url += (bank_type!=null ? "bank_type="+bank_type : "")
                + (begindate!=null ? "&begindate="+begindate : "")
                + (enddate!=null ? "&enddate="+enddate : "")
                + (accno!=null ? "&accno="+accno : "");

        String jsonStr;
        try {
            jsonStr = httpService.httpGet(url);
        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(httpService.RESPONSE_STATUS_CODE_KEY, httpService.RESPONSE_STATUS_CODE_ERROR);
            resultMap.put(httpService.RESPONSE_STATUS_MESSAGE_KEY, e.getMessage());

            jsonStr = JSONObject.toJSONString(resultMap);
            logger.error(e.getMessage());
        }

        return jsonStr;
    }

    @ResponseBody
    @RequestMapping(value = "/balance/import", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String importBankBalance(@RequestBody String requestJson){
        String url = b2eHost+"/api/bank/balances/remote?";

        JSONObject jsonObject = JSONObject.parseObject(requestJson);
        String bank_type = (String) jsonObject.get("bank_type");
        String datetime = (String) jsonObject.get("datetime");
        String accno = (String) jsonObject.get("accno");

        url += (bank_type!=null ? "bank_type="+bank_type : "")
                + (datetime!=null ? "&datetime="+datetime : "")
                + (accno!=null ? "&accno="+accno : "");

        String jsonStr;
        try {
            jsonStr = httpService.httpGet(url);
        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(httpService.RESPONSE_STATUS_CODE_KEY, httpService.RESPONSE_STATUS_CODE_ERROR);
            resultMap.put(httpService.RESPONSE_STATUS_MESSAGE_KEY, e.getMessage());

            jsonStr = JSONObject.toJSONString(resultMap);
            logger.error(e.getMessage());
        }

        return jsonStr;
    }

    @ResponseBody
    @RequestMapping(value = "/front-service", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String listBankFrontService(){
        String url = b2eHost+"/api/service?flag=1";

        String jsonStr;
        try {
            jsonStr = httpService.httpGet(url);
        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(httpService.RESPONSE_STATUS_CODE_KEY, httpService.RESPONSE_STATUS_CODE_ERROR);
            resultMap.put(httpService.RESPONSE_STATUS_MESSAGE_KEY, e.getMessage());

            jsonStr = JSONObject.toJSONString(resultMap);
            logger.error(e.getMessage());
        }

        return jsonStr;
    }

    @ResponseBody
    @RequestMapping(value = "/front-service/detail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String getBankFrontServiceDetail(String bank_type){
        String url = b2eHost+"/api/service/info?";

        url += (bank_type!=null ? "bank_type="+bank_type : "");

        String jsonStr;
        try {
            jsonStr = httpService.httpGet(url);
        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(httpService.RESPONSE_STATUS_CODE_KEY, httpService.RESPONSE_STATUS_CODE_ERROR);
            resultMap.put(httpService.RESPONSE_STATUS_MESSAGE_KEY, e.getMessage());

            jsonStr = JSONObject.toJSONString(resultMap);
            logger.error(e.getMessage());
        }

        return jsonStr;
    }

    @ResponseBody
    @RequestMapping(value = "/front-service/log", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String getBankFrontServiceLog(String bank_type, String day){
        String url = b2eHost+"/api/service/logs?flag=1";

        url += (bank_type!=null ? "&bank_type="+bank_type : "")
            + (day!=null ? "&data="+day : "");

        String jsonStr;
        try {
            jsonStr = httpService.httpGet(url);
        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(httpService.RESPONSE_STATUS_CODE_KEY, httpService.RESPONSE_STATUS_CODE_ERROR);
            resultMap.put(httpService.RESPONSE_STATUS_MESSAGE_KEY, e.getMessage());

            jsonStr = JSONObject.toJSONString(resultMap);
            logger.error(e.getMessage());
        }

        return jsonStr;
    }
}
