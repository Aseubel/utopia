package com.aseubel.types.exception;

import com.aseubel.types.enums.GlobalServiceStatusCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 611611611611611611L;

    /** 异常码 */
    private String code;

    /** 异常信息 */
    private String info;

    public AppException(String info) {
        this.code = String.valueOf(HttpStatus.BAD_REQUEST.value());
        this.info = info;
    }

    public AppException(String code, Throwable cause) {
        this.code = code;
        super.initCause(cause);
    }

    public AppException(String code, String message) {
        this.code = code;
        this.info = message;
    }

    public AppException(String code, String message, Throwable cause) {
        this.code = code;
        this.info = message;
        super.initCause(cause);
    }

    public AppException(Integer code, String message, Throwable cause) {
        this.code = String.valueOf(code);
        this.info = message;
        super.initCause(cause);
    }

    public AppException(Integer code, String message) {
        this.code = String.valueOf(code);
        this.info = message;
    }

    public AppException(GlobalServiceStatusCode globalServiceStatusCode) {
        this.code = globalServiceStatusCode.getCode().toString();
        this.info = globalServiceStatusCode.getMessage();
    }

    public AppException(GlobalServiceStatusCode globalServiceStatusCode, Throwable cause) {
        this.code = globalServiceStatusCode.getCode().toString();
        this.info = globalServiceStatusCode.getMessage();
        super.initCause(cause);
    }

}
