package com.aseubel.infrastructure.util;

import com.aseubel.domain.user.adapter.web.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * @author Aseubel
 * @description HttpClientUtil
 * @date 2025-01-12 20:10
 */
@Component
@Slf4j
public class HttpClientUtil implements HttpClient {

    /**
     * 发送GET方式请求
     * @param url 请求的url
     * @param paramMap 请求参数
     * @return 返回响应结果
     */
    @Override
    public String doGet(String url,Map<String,String> paramMap){
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String result = "";
        CloseableHttpResponse response = null;

        try{
            URIBuilder builder = new URIBuilder(url);
            if(paramMap != null){
                for (String key : paramMap.keySet()) {
                    builder.addParameter(key,paramMap.get(key));
                }
            }
            URI uri = builder.build();

            //创建GET请求
            HttpGet httpGet = new HttpGet(uri);

            //发送请求
            response = httpClient.execute(httpGet);

            //判断响应状态
            if(response.getStatusLine().getStatusCode() == 200){
                result = EntityUtils.toString(response.getEntity(),"UTF-8");
            }
        }catch (Exception e){
            log.error("请求失败",e);
        }finally {
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                log.error("关闭httpClient连接失败",e);
            }
        }

        return result;
    }

}
