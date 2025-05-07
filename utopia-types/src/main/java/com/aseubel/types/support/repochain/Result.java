package com.aseubel.types.support.repochain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @description 审批结果
 * @date 2025/4/28 上午1:14
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public final class Result<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return Result.<T>builder().success(true).message("success").data(data).build();
    }

    public static <T> Result<T> fail(T data, String message) {
        return Result.<T>builder().success(false).message(message).data(data).build();
    }
}
