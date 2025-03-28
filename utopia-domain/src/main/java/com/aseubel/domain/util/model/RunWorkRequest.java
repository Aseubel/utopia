package com.aseubel.domain.util.model;

import cn.hutool.json.JSONUtil;
import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static com.aseubel.types.common.Constant.DIFY_WORK_FLOW_RESPONSE_MODE;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunWorkRequest {

    @FieldDesc(name = "输入")
    private String inputs;

    @FieldDesc(name = "响应模式")
    private String response_mode;

    @FieldDesc(name = "用户标识")
    private String user;

    public RunWorkRequest(String inputs, String user) {
        this.inputs = inputs;
        this.response_mode = DIFY_WORK_FLOW_RESPONSE_MODE;
        this.user = user;
    }

    public RunWorkRequest(String inputs) {
        this.inputs = inputs;
        this.response_mode = DIFY_WORK_FLOW_RESPONSE_MODE;
        this.user = System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);
    }

    public String toJson() {
        return "{" + "\"inputs\":" + inputs + ","
                + "\"response_mode\":\"" + response_mode + "\","
                + "\"user\":\"" + user + "\"" + "}";
    }
}
