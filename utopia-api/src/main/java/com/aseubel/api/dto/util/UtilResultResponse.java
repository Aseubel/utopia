package com.aseubel.api.dto.util;

import com.aseubel.types.annotation.FieldDesc;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UtilResultResponse implements Serializable {

    @FieldDesc(name = "status")
    private String status;

    @FieldDesc(name = "result")
    private Object result;

    @FieldDesc(name = "错误原因")
    private String errorReason;

    public UtilResultResponse(String[] resultStatus) {
        this.status = resultStatus[0];
        if (resultStatus[0].equals("failed")) {
            this.errorReason = resultStatus[1];
        } else if (resultStatus[0].equals("succeeded")) {
            HashMap<String, String> resultMap = new HashMap<>();
            JsonObject jsonObject = JsonParser.parseString(resultStatus[1]).getAsJsonObject();
            Set<String> keySet = jsonObject.keySet();
            for (String key : keySet) {
                if (jsonObject.get(key) instanceof JsonArray jsonArray) {
                    resultMap.put(key, jsonArray.get(0).getAsJsonObject().get("url").getAsString());
                } else {
                    resultMap.put(key, jsonObject.get(key).getAsString());
                }
            }
            this.result = resultMap;
        } else {
            this.result = resultStatus[1];
        }
    }
}
