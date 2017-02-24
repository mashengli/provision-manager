package com.qdb.provmgr.service.ccb;

import com.qdb.provmgr.dao.ccb.CCBReportDao;
import com.qdb.provmgr.dao.entity.report.DataTable1_3;
import com.qdb.provmgr.dao.entity.report.DataTable3Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yuwenzhong on 2016-11-23.
 */
@Service
public class CCBReportService {

    @Autowired
    private CCBReportDao ccbReportDao;

    /**
     * 查询表1、2、3、6、9、10数据
     * @param tableType
     * @param bankName
     * @param name
     * @param adId
     * @param accountNo
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> findTableDataList(String tableType, String bankName, String name, String adId, String accountNo, String beginDate, String endDate){
        List<Map<String, Object>> dataList = ccbReportDao.queryDataList(tableType, bankName, name, adId, accountNo, beginDate, endDate);
        if (CollectionUtils.isEmpty(dataList)){
            dataList = new ArrayList<>();
        }else if (dataList.size() < 31){
            this.buildFullDataList(dataList);
        }
        return dataList;
    }

    /**
     * 报表数据按每月31天上报，不存在则以0填充
     * @param dataList
     */
    private void buildFullDataList(List<Map<String, Object>> dataList) {
        int days = dataList.size();
        int addCount = 31 - days;
        Map<String, Object> dataMap = dataList.get(0);
        Set<String> keySet = dataMap.keySet();
        for (int i = 1; i <= addCount; i++) {
            Map<String, Object> map = new HashMap();
            for (String key : keySet) {
                Object obj = dataMap.get(key);
                switch (key){
                    case "bankName_S" :
                    case "AD" :
                    case "ADID" :
                    case "name" :
                        map.put(key, obj);
                        break;
                    case "natuDate" :
                        map.put(key, days + i);
                        break;
                    default:
                        map.put(key, new BigDecimal("0"));
                }
            }
            dataList.add(map);
        }
    }

    /**
     * 封装表3主体数据
     * @param tableType
     * @param bankName
     * @param name
     * @param adId
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<DataTable3Entity> findTable3Data(String tableType, String bankName, String name, String adId, String beginDate, String endDate){
        List<DataTable3Entity> resultList = null;

        List<Map<String, Object>> bankInfoList = ccbReportDao.queryBankInfoList(null, null, bankName);
        if(!CollectionUtils.isEmpty(bankInfoList)){
            resultList = new ArrayList<>();
            for (Map<String, Object> bankMap : bankInfoList) {
                String accountNo = (String) bankMap.get("AD");
                List<Map<String, Object>> dataList = this.findTableDataList(tableType, bankName, name, adId, accountNo, beginDate, endDate);
                DataTable3Entity table3Entity = this.convertResult2Entity(dataList);
                resultList.add(table3Entity);
            }
        }
    return resultList;
}

    /**
     * 针对表3的数据，封装单行数据对象
     * @param list
     * @return
     */
    public DataTable3Entity convertResult2Entity(List<Map<String, Object>> list){
        DataTable3Entity rowEntity13 = new DataTable3Entity();
        List<DataTable1_3> rowC01List = new ArrayList<>();
        int days = list.size();
        for (int i = 0; i < days; i++) {
            DataTable1_3 dataTable3 = new DataTable1_3();

            Map<String, Object> map = list.get(i);
            dataTable3.setC01((BigDecimal) map.get("C01"));
            rowC01List.add(dataTable3);
        }

        Map<String, Object> map = list.get(0);
        String bankName = (String) map.get("bankName_S");
        String accountNo = (String) map.get("AD");
        List<Map<String, Object>> bankInfoList = ccbReportDao.queryBankInfoList(null, accountNo, bankName);
        String bankBranch = bankName;
        if (!CollectionUtils.isEmpty(bankInfoList)){
            Map<String, Object> bankMap = bankInfoList.get(0);
            bankBranch = (String) bankMap.get("branch");
        }
        rowEntity13.setBankName(bankBranch);
        rowEntity13.setAccountNo(accountNo);
        rowEntity13.setAccountName((String) map.get("name"));
        rowEntity13.setList(rowC01List);
        return rowEntity13;
    }

}
