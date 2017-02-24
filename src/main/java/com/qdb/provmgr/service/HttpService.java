package com.qdb.provmgr.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hezhongchen on 2016/12/15.
 */
@Service
public class HttpService {
    private Logger logger = LoggerFactory.getLogger(HttpService.class);

    private final int REQUEST_STATUS_CODE_SUCCESS = 200;

    public final int RESPONSE_STATUS_CODE_SUCCESS = 200;
    public final int RESPONSE_STATUS_CODE_ERROR = 400;
    public final String RESPONSE_STATUS_CODE_KEY = "code";
    public final String RESPONSE_STATUS_MESSAGE_KEY = "message";

    public String httpGet(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet httpGet = new HttpGet(url);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000).build();
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();
        logger.info("GET "+url+", status code:"+statusCode);

        String jsonStr;
        if (statusCode==REQUEST_STATUS_CODE_SUCCESS) {
            HttpEntity httpEntity = response.getEntity();
            jsonStr = EntityUtils.toString(httpEntity);
        } else {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put(RESPONSE_STATUS_CODE_KEY, RESPONSE_STATUS_CODE_ERROR);
            resultMap.put(RESPONSE_STATUS_MESSAGE_KEY, response.getStatusLine().getReasonPhrase());

            jsonStr = JSONObject.toJSONString(resultMap);
        }

        httpGet.releaseConnection();

        return jsonStr;
    }
}
