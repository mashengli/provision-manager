package com.qdb.provmgr.dao.ccb;

import com.qdb.provmgr.report.ccb.CCBReportConstant.ITemplateFileNames;
import com.qdb.provmgr.dao.DBUtil;
import com.qdb.provmgr.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yuwenzhong on 2016-11-25.
 */
@Repository
public class CCBReportDao {

    @Autowired
    private DBUtil dbUtil;

    /**
     * 查询表1、2、3、6、9、10数据
     * @param tableType 表名
     * @param bankName 银行名称
     * @param name 机构名称
     * @param adId 账户Id
     * @param accountNo 账户号
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return
     */
    public List<Map<String, Object>> queryDataList(String tableType, String bankName, String name, String adId, String accountNo, String beginDate, String endDate){
        List<String> paramList = new ArrayList<>(10);
        StringBuffer sqlBuf = this.buildBaseSql(tableType, bankName, name, adId, accountNo, beginDate, endDate, paramList);
        Object[] params = paramList.toArray();
        List<Map<String, Object>> dataList = dbUtil.queryForList(sqlBuf.toString(), params);
        return dataList;
    }

    /**
     * 根据银行名称查询备付金账户银行信息
     * @param adId
     * @param accountNo
     * @param bankName
     */
    public List<Map<String, Object>> queryBankInfoList(String adId, String accountNo, String bankName){

        StringBuffer sqlBuf = new StringBuffer(" SELECT t.ADID, t.AD, t.bankName_S, t.branch " +
                        " FROM UacAutoCheck.ProvMgr.info_ADinfo t " +
                        " WHERE t.isProvision='1' ");

        List<String> paramList = new ArrayList<>(5);
        if (StringUtils.isNotEmpty(bankName)){
            sqlBuf.append(" AND t.bankName_S = ? ");
            paramList.add(bankName);
        }

        if (StringUtils.isNotEmpty(adId)){
            sqlBuf.append(" AND t.ADID = ? ");
            paramList.add(adId);
        }

        if (StringUtils.isNotEmpty(accountNo)){
            sqlBuf.append(" AND t.AD = ? ");
            paramList.add(accountNo);
        }
        sqlBuf.append(" ORDER BY t.ADID ");

        Object[] params = paramList.toArray();
        List<Map<String, Object>> resultList = dbUtil.queryForList(sqlBuf.toString(), params);
        return resultList;
    }

    /**
     * 拼接sql
     * @param tableType 表名
     * @param bankName 银行名称
     * @param accountNo 账户号
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return sql
     */
    private StringBuffer buildBaseSql(String tableType, String bankName, String name, String adId, String accountNo, String beginDate, String endDate, List<String> paramList){
        StringBuffer sqlBuf = new StringBuffer();
        int n = 10000;
        if(ITemplateFileNames.T1_1.equals(tableType)){
            sqlBuf.append(" SELECT t.bankName_S, t.ADID, t.name, t.AD, DAY(t.natuDate) as 'natuDate', " +
                    " CAST(ROUND(t.A01/" + n + ", 6) AS numeric(20,6)) A01, CAST(ROUND(t.A02/" + n + ", 6) AS numeric(20,6)) A02, CAST(ROUND(t.A03/" + n + ", 6) AS numeric(20,6)) A03, " +
                    " CAST(ROUND(t.A04/" + n + ", 6) AS numeric(20,6)) A04, CAST(ROUND(t.A05/" + n + ", 6) AS numeric(20,6)) A05, CAST(ROUND(t.A06/" + n + ", 6) AS numeric(20,6)) A06, " +
                    " CAST(ROUND(t.A07/" + n + ", 6) AS numeric(20,6)) A07, CAST(ROUND(t.A08/" + n + ", 6) AS numeric(20,6)) A08, CAST(ROUND(t.A09/" + n + ", 6) AS numeric(20,6)) A09, " +
                    " CAST(ROUND(t.A10/" + n + ", 6) AS numeric(20,6)) A10, CAST(ROUND(t.A11/" + n + ", 6) AS numeric(20,6)) A11, CAST(ROUND(t.A12/" + n + ", 6) AS numeric(20,6)) A12, " +
                    " CAST(ROUND(t.A13/" + n + ", 6) AS numeric(20,6)) A13, CAST(ROUND(t.A14/" + n + ", 6) AS numeric(20,6)) A14 " +
                    " FROM UacAutoCheck.ProvMgr.report_provision_cent_1_1 t WHERE 1=1 ");
        }

        if(ITemplateFileNames.T1_2.equals(tableType)){
            sqlBuf.append(" SELECT t.bankName_S, t.ADID, DAY(t.natuDate) as 'natuDate', " +
                    " CAST(ROUND(t.BB01/" + n + ",6) AS numeric(20,6)) BB01, CAST(ROUND(t.BB02/" + n + ",6) AS numeric(20,6)) BB02, CAST(ROUND(t.BB03/" + n + ",6) AS numeric(20,6)) BB03, " +
                    " CAST(ROUND(t.BB04/" + n + ",6) AS numeric(20,6)) BB04, CAST(ROUND(t.BB05/" + n + ",6) AS numeric(20,6)) BB05, CAST(ROUND(t.BB06/" + n + ",6) AS numeric(20,6)) BB06, " +
                    " CAST(ROUND(t.BB07/" + n + ",6) AS numeric(20,6)) BB07, CAST(ROUND(t.BB08/" + n + ",6) AS numeric(20,6)) BB08, CAST(ROUND(t.BB09/" + n + ",6) AS numeric(20,6)) BB09 " +
                    " FROM UacAutoCheck.ProvMgr.report_provision_cent_1_2_1 t WHERE 1=1 ");
        }

        if(ITemplateFileNames.T1_3.equals(tableType)){
            sqlBuf.append(" SELECT t.bankName_S, t.AD, t.name, DAY(t.natuDate) as 'natuDate', CAST(ROUND(t.C01/" + n + ",6) AS numeric(20,6)) C01" +
                    " FROM UacAutoCheck.ProvMgr.report_provision_cent_1_3 t WHERE 1=1 ");
        }

        if(ITemplateFileNames.T1_6.equals(tableType)){
            sqlBuf.append("SELECT t.bankName_S, t.ADID, t.name, t.AD, DAY(t.natuDate) as 'natuDate', " +
                    " CAST(ROUND(t.F01/" + n + ",6) AS numeric(20,6)) F01, CAST(ROUND(t.F02/" + n + ",6) AS numeric(20,6)) F02, CAST(ROUND(t.F03/" + n + ",6) AS numeric(20,6)) F03, " +
                    " CAST(ROUND(t.F04/" + n + ",6) AS numeric(20,6)) F04, CAST(ROUND(t.F05/" + n + ",6) AS numeric(20,6)) F05, CAST(ROUND(t.F06/" + n + ",6) AS numeric(20,6)) F06, " +
                    " CAST(ROUND(t.F07/" + n + ",6) AS numeric(20,6)) F07, CAST(ROUND(t.F08/" + n + ",6) AS numeric(20,6)) F08, CAST(ROUND(t.F09/" + n + ",6) AS numeric(20,6)) F09, " +
                    " CAST(ROUND(t.F10/" + n + ",6) AS numeric(20,6)) F10, CAST(ROUND(t.G01/" + n + ",6) AS numeric(20,6)) G01, CAST(ROUND(t.G02/" + n + ",6) AS numeric(20,6)) G02, " +
                    " CAST(ROUND(t.G03/" + n + ",6) AS numeric(20,6)) G03, CAST(ROUND(t.G04/" + n + ",6) AS numeric(20,6)) G04, CAST(ROUND(t.G05/" + n + ",6) AS numeric(20,6)) G05, " +
                    " CAST(ROUND(t.G06/" + n + ",6) AS numeric(20,6)) G06, CAST(ROUND(t.G07/" + n + ",6) AS numeric(20,6)) G07, CAST(ROUND(t.G08/" + n + ",6) AS numeric(20,6)) G08, " +
                    " CAST(ROUND(t.G09/" + n + ",6) AS numeric(20,6)) G09, CAST(ROUND(t.G10/" + n + ",6) AS numeric(20,6)) G10, CAST(ROUND(t.G11/" + n + ",6) AS numeric(20,6)) G11, " +
                    " CAST(ROUND(t.G12/" + n + ",6) AS numeric(20,6)) G12, CAST(ROUND(t.G13/" + n + ",6) AS numeric(20,6)) G13, CAST(ROUND(t.G14/" + n + ",6) AS numeric(20,6)) G14 " +
                    " FROM UacAutoCheck.ProvMgr.report_provision_cent_1_6 t WHERE 1=1 " );
        }

        if(ITemplateFileNames.T1_9.equals(tableType)){
            sqlBuf.append(" SELECT  t.bankName_S, t.ADID, t.name, t.AD, " +
                    " DAY(t.natuDate) as 'natuDate', CAST(ROUND(t.J01/" + n + ",6) AS numeric(20,6)) J01, CAST(ROUND(t.J02/" + n + ",6) AS numeric(20,6)) J02, " +
                    " CAST(ROUND(t.J03/" + n + ",6) AS numeric(20,6)) J03, CAST(ROUND(t.J04/" + n + ",6) AS numeric(20,6)) J04 " +
                    " FROM UacAutoCheck.ProvMgr.report_provision_cent_1_9 t WHERE 1=1 ");
        }

        if(ITemplateFileNames.T1_10.equals(tableType)){
            sqlBuf.append(" SELECT bankName_S, t.ADID, t.name, t.AD, DAY(t.natuDate) as 'natuDate', " +
                    " t.K01, CAST(ROUND(t.K02/" + n + ",6) AS numeric(20,6)) K02, t.K03, CAST(ROUND(t.K04/" + n + ",6) AS numeric(20,6)) K04, " +
                    " t.K05, CAST(ROUND(t.K06/" + n + ",6) AS numeric(20,6)) K06, t.K07, CAST(ROUND(t.K08/" + n + ",6) AS numeric(20,6)) K08, " +
                    " t.K09, CAST(ROUND(t.K10/" + n + ",6) AS numeric(20,6)) K10, t.K11, CAST(ROUND(t.K12/" + n + ",6) AS numeric(20,6)) K12, " +
                    " t.K13, CAST(ROUND(t.K14/" + n + ",6) AS numeric(20,6)) K14, t.K15, CAST(ROUND(t.K16/" + n + ",6) AS numeric(20,6)) K16, " +
                    " t.K17, CAST(ROUND(t.K18/" + n + ",6) AS numeric(20,6)) K18, t.K19, CAST(ROUND(t.K20/" + n + ",6) AS numeric(20,6)) K20, " +
                    " t.K21, CAST(ROUND(t.K22/" + n + ",6) AS numeric(20,6)) K22, t.K23, CAST(ROUND(t.K24/" + n + ",6) AS numeric(20,6)) K24 " +
                    " FROM UacAutoCheck.ProvMgr.report_provision_cent_1_10 t WHERE 1=1 " );
        }

        if (StringUtils.isNotEmpty(bankName)){
            sqlBuf.append(" AND t.bankName_S = ? ");
            paramList.add(bankName);
        }

        if(StringUtils.isNotEmpty(name)){
            sqlBuf.append(" AND t.name = ? ");
            paramList.add(name);
        }

        if (StringUtils.isNotEmpty(adId)){
            sqlBuf.append(" AND t.ADID = ? ");
            paramList.add(adId);
        }

        if (StringUtils.isNotEmpty(accountNo)){
            sqlBuf.append(" AND t.AD = ? ");
            paramList.add(accountNo);
        }

        if (StringUtils.isNotEmpty(beginDate)){
            sqlBuf.append(" AND t.natuDate >= ? ");
            paramList.add(beginDate);
        }

        if (StringUtils.isNotEmpty(endDate)){
            sqlBuf.append(" AND t.natuDate <= ? ");
            paramList.add(endDate);
        }
        sqlBuf.append(" ORDER BY t.natuDate ");
        return sqlBuf;
    }

}
