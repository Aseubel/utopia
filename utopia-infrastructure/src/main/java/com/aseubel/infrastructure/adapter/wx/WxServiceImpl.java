package com.aseubel.infrastructure.adapter.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aseubel.domain.user.adapter.web.HttpClient;
import com.aseubel.domain.user.adapter.wx.WxService;
import com.aseubel.types.exception.WxException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.aseubel.types.common.Constant.WX_LOGIN;

/**
 * @author Aseubel
 * @description 微信服务实现类
 * @date 2025-01-12 19:10
 */
@Component
public class WxServiceImpl implements WxService {

    @Resource
    private HttpClient httpClient;

    @Override
    public String getOpenid(String appid, String secret, String code) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", appid);
        paramMap.put("secret", secret);
        paramMap.put("js_code", code);
        paramMap.put("grant_type", "authorization_code");
        String result = httpClient.doGet(WX_LOGIN, paramMap);

        //获取请求结果
        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.getString("openid");
        //判断openid是否存在
        if (StringUtils.isEmpty(openid)) {
            throw new WxException(jsonObject.getString("errcode"), jsonObject.getString("errmsg"));
        }

        return openid;
    }

}
