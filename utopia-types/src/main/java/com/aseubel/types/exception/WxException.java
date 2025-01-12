package com.aseubel.types.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Aseubel
 * @description 微信相关异常
 * @date 2025-01-12 18:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WxException extends RuntimeException {

    private static final long serialVersionUID = 232323666666666L;

    /** 异常码 */
    private String code;

    /** 异常信息 */
    private String info;

    public WxException(String code) {
        this.code = code;
    }

    public WxException(String code, Throwable cause) {
        this.code = code;
        super.initCause(cause);
    }

    public WxException(String code, String message) {
        this.code = code;
        this.info = message;
    }

    public WxException(String code, String message, Throwable cause) {
        this.code = code;
        this.info = message;
        super.initCause(cause);
    }

}
