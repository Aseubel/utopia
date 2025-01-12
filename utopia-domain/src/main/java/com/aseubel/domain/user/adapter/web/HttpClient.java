package com.aseubel.domain.user.adapter.web;

import java.util.Map;

/**
 * @author Aseubel
 * @description HttpClient interface
 * @date 2025-01-12 20:09
 */
public interface HttpClient {

    /**
     * 发送GET方式请求
     * @param url 请求的url
     * @param paramMap 请求参数
     * @return 返回响应结果
     */
    String doGet(String url, Map<String, String> paramMap);

}
