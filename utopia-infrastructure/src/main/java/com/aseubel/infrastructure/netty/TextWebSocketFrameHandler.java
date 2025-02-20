package com.aseubel.infrastructure.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aseubel.types.exception.WxException;
import com.aseubel.types.util.HttpClientUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.aseubel.types.common.Constant.WX_LOGIN;

@ChannelHandler.Sharable
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Value("${wechat.config.appid:aseubel-appid}")
    private String appid;

    @Value("${wechat.config.secret:aseubel-secret}")
    private String secret;

    // 会话管理器，记录在线用户（openid -> Channel）
    private static final Map<String, Channel> userChannels = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;
            String queryString = req.uri().split("\\?")[1];
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(queryString, StandardCharsets.UTF_8);
            String code = queryStringDecoder.parameters().get("code").get(0);
            // 将 code 存入 Channel 属性
            ctx.channel().attr(AttributeKey.valueOf("code")).set(code);
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        String message = frame.text();
        // 解析消息（假设消息格式为 {"toUserId":"xxx","content":"hello"}）
        JsonObject json = JsonParser.parseString(message).getAsJsonObject();
        String toUserId = json.get("toUserId").getAsString();
        String content = json.get("content").getAsString();

        // 查找目标用户的 Channel
        Channel targetChannel = userChannels.get(toUserId);
        if (targetChannel != null && targetChannel.isActive()) {
            targetChannel.writeAndFlush(new TextWebSocketFrame(content));
        }
    }

    // 绑定用户 openid
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String code = getCodeFromRequest(ctx); // 从请求中提取 code
        String openid = code;// validateCode(code);    // 验证 code 获取 openid
        userChannels.put(openid, ctx.channel());
    }

    // 处理连接断开
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        userChannels.values().remove(ctx.channel());
    }

    private String validateCode(String code) {
        return getOpenid(appid, secret, code);
    }

    // 从 HTTP 请求中提取 code（ URL 参数）
    private String getCodeFromRequest(ChannelHandlerContext ctx) {
//        // 检查 request 是否存在
//        FullHttpRequest req = (FullHttpRequest) ctx.channel().attr(AttributeKey.valueOf("request")).get();
//        if (req == null) {
//            throw new IllegalArgumentException("Request attribute is missing");
//        }
//
//        // 解析 URL 参数
//        QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
//        List<String> codeValues = decoder.parameters().get("code");
        String code = (String) ctx.channel().attr(AttributeKey.valueOf("code")).get();

        // 检查 code 参数是否存在且非空
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Code parameter is missing or empty");
        }

        return code;
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
