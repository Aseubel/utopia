package com.aseubel.types.util;

import okhttp3.*;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class OkHttpUtil {
    // 单例OkHttpClient（推荐复用实例）
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS) // SSE需要无限读取超时
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * 普通GET请求
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求参数
     */
    public static String doGet(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        addHeaders(builder, headers);
        if (params != null) {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            params.forEach(urlBuilder::addQueryParameter);
            builder.url(urlBuilder.build());
        }

        try (Response response = okHttpClient.newCall(builder.build()).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    /**
     * 普通GET请求
     * @param url 请求地址
     * @param headers 请求头
     */
    public static String doGet(String url, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        addHeaders(builder, headers);

        try (Response response = okHttpClient.newCall(builder.build()).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    /**
     * 发起SSE POST请求
     * @param url 请求地址
     * @param headers 请求头
     * @param requestBody 请求体
     * @param eventListener 事件监听器
     */
    public static void postSSE(String url,
                               Map<String, String> headers,
                               RequestBody requestBody,
                               SSEEventListener eventListener) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);

        // 添加请求头
        if (headers != null) {
            headers.forEach(requestBuilder::addHeader);
        }

        // 发起异步请求
        okHttpClient.newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                eventListener.onFailure(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    eventListener.onFailure(new IOException("Unexpected code " + response));
                    return;
                }

                // 验证Content-Type
                MediaType contentType = response.body().contentType();
                if (contentType == null || !contentType.toString().contains("text/event-stream")) {
                    eventListener.onFailure(new IOException("Invalid content type: " + contentType));
                    return;
                }

                try (Buffer buffer = new Buffer();
                     ResponseBody body = response.body()) {
                    if (body == null) return;

                    while (!call.isCanceled()) {
                        String line = body.source().readUtf8Line();
                        if (line == null) break;

                        // 触发事件处理
                        if (line.startsWith("data:")) {
                            String data = line.substring(5).trim();
                            eventListener.onEvent(data);
                        }
                        // 其他事件类型处理...
                    }

                    eventListener.onComplete();
                } catch (Exception e) {
                    eventListener.onFailure(e);
                }
            }
        });
    }

    /**
     * SSE事件监听接口
     */
    public interface SSEEventListener {
        void onEvent(String eventData);
        void onComplete();
        void onFailure(Throwable t);
    }

    // 添加请求头通用方法
    private static void addHeaders(Request.Builder builder, Map<String, String> headers) {
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 创建JSON请求体
     */
    public static RequestBody createJsonBody(String json) {
        return RequestBody.create(json, MediaType.parse("application/json"));
    }

    /**
     * 创建表单请求体
     */
    public static RequestBody createFormBody(Map<String, String> formData) {
        FormBody.Builder builder = new FormBody.Builder();
        formData.forEach(builder::add);
        return builder.build();
    }
}

