package com.qdb.provmgr.controller;

import com.alibaba.fastjson.JSONObject;
import com.qdb.provmgr.inter.FileListFilter;
import com.qdb.provmgr.service.ReportConvertService;
import com.qdb.provmgr.util.constant.ReportConvertConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenzhong on 2017/2/17.
 */
@Controller
@RequestMapping(value = "/provsion/report")
public class ReportConvertController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ReportConvertService convertService;

    @ResponseBody
    @RequestMapping(value = "/convert", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String reportConvert(HttpServletRequest request){
        Map<Integer, String> resultMap = new HashMap<>();
        try {
            String rootPath = "/Users/wenzhong/Documents/5月/20150501_20150531_备付金_[BJ0000004]北京钱袋宝支付技术有限公司";
            File rootFile = new File(rootPath);
//            this.classifyFiles(rootFile);

            //考虑加入多线程
            convertService.createFiles(request, ReportConvertConstant.ROOT_DIR);
            resultMap.put(200, "success");
        } catch (Exception e) {
            logger.error("----------------:{}", e);
            resultMap.put(400, "failure");
        }

        return JSONObject.toJSONString(resultMap);
    }


    /**
     * 遍历目录下的文件
     * @param rootFile
     */
    private void classifyFiles(File rootFile) {
        File[] files = rootFile.listFiles(new FileListFilter());
        for (File file: files) {
            if(file.isDirectory()){
                this.classifyFiles(file);
            }else{
                convertService.fileGrouping(file);
            }
        }
    }
}
