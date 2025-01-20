package com.aseubel.types.exception;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.types.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aseubel.types.enums.GlobalServiceStatusCode.PARAM_FAILED_VALIDATE;

/**
 * @author chensongmin
 * @description 全局异常处理器
 * @date 2024/11/11
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    /**
     * 自定义验证异常 MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T> Response<T> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("参数校验异常", ex);
        String message = ex.getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .filter(Objects::nonNull)
            .collect(Collectors.joining("\n"));

        return Response.CUSTOMIZE_MSG_ERROR(PARAM_FAILED_VALIDATE, message);
    }

    /**
     *
     * 自定义验证异常 ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T> Response<T> constraintViolationException(ConstraintViolationException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("参数校验异常", e);
        String message = e.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .filter(Objects::nonNull)
            .collect(Collectors.joining("\n"));

        return Response.CUSTOMIZE_MSG_ERROR(PARAM_FAILED_VALIDATE, message);
    }

    /**
     * 自定义验证异常 BindException
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T> Response<T> handleBindException(BindException ex) {
        log.error("参数绑定异常", ex);
        String message = ex.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));

        return Response.CUSTOMIZE_MSG_ERROR(PARAM_FAILED_VALIDATE, message);
    }

    /**
     * 捕获全局业务异常 AppException
     */
    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.OK)
    public <T> Response<T> handleAPPException(AppException e) {
        log.error("业务服务异常, code:{}, message:{}", e.getCode(), e.getInfo());
        return Response.APP_EXCEPTION(e);
    }

    /**
     * 捕获全局微信服务异常 WxException
     */
    @ExceptionHandler(WxException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public <T> Response<T> handleAPPException(WxException e) {
        log.error("微信服务异常, code:{}, message:{}", e.getCode(), e.getInfo());
        return Response.WX_EXCEPTION(e);
    }

    /**
     * 捕获阿里云OSS服务异常 AliException
     */
    @ExceptionHandler(ClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public <T> Response<T> handleAPPException(ClientException e) {
        log.error("阿里云OSS服务异常, code:{}, message:{}", e.getErrCode(), e.getErrMsg());
        return Response.Ali_EXCEPTION(e);
    }
}
