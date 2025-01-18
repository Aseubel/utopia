package com.aseubel.types.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Aseubel
 * @description 阿里云服务相关异常
 * @date 2025-01-18 12:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AliException extends RuntimeException {

    private static final long serialVersionUID = 232323828282828L;

    /** 异常码 */
    private String code;

    /** 异常信息 */
    private String info;

    public AliException(String code) {
        this.code = code;
    }

    public AliException(String code, Throwable cause) {
        this.code = code;
        super.initCause(cause);
    }

    public AliException(String code, String message) {
        this.code = code;
        this.info = message;
    }

    public AliException(String code, String message, Throwable cause) {
        this.code = code;
        this.info = message;
        super.initCause(cause);
    }

}
