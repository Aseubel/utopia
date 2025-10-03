package com.aseubel.infrastructure.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.exception.WxException;
import com.aseubel.types.util.HttpClientUtil;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.aseubel.types.common.Constant.*;

/**
 * @author Aseubel
 * @description 处理websocket连接请求，将code参数存入channel的attribute中
 * @date 2025-02-21 15:34
 */
public class SessionHandler extends ChannelInboundHandlerAdapter {

    public static final Map<String, Channel> userChannels = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 判断是否是连接请求
        if (msg instanceof FullHttpRequest request) {
            try {
                QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
                String code = decoder.parameters().get("code").get(0); // 从请求中提取 code
                String userId = code;// getOpenid(code);    // 验证 code 获取 openid

                ctx.channel().attr(WS_USER_ID_KEY).set(userId);
                userChannels.put(userId, ctx.channel());
                // 重新设置 uri，将请求转发到 websocket handler，否则无法成功建立连接
                request.setUri("/ws");
                ctx.fireChannelRead(request);
            } catch (Exception e) {
                throw new AppException("非法的websocket连接请求" + e.getMessage());
            }
        } else {
            // 消息直接交给下一个 handler
            ctx.fireChannelRead(msg);
        }
    }

    private String getOpenid(String appid, String secret, String code) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", appid);
        paramMap.put("secret", secret);
        paramMap.put("js_code", code);
        paramMap.put("grant_type", "authorization_code");
        String result = HttpClientUtil.doGet(WX_LOGIN, paramMap);

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
