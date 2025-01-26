package com.aseubel.types;

import com.aliyun.oss.OSSException;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.types.enums.GlobalServiceStatusCode;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.exception.WxException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Optional;

import static com.aseubel.types.enums.GlobalServiceStatusCode.OSS_DOWNLOAD_ERROR;

@Data
@Builder
@AllArgsConstructor
public final class Response<T> implements Serializable {

    private static final long serialVersionUID = 7000723935764546321L;

    private Integer code;
    private String info;
    private T data;

    private Response() {
    }

    /**
     * 成功信息返回, 无数据 {@link GlobalServiceStatusCode#SYSTEM_SUCCESS}
     *
     * @return 成功状态码
     */
    public static <T> Response<T> SYSTEM_SUCCESS() {
        return Response.<T>builder()
                .code(GlobalServiceStatusCode.SYSTEM_SUCCESS.getCode())
                .info(GlobalServiceStatusCode.SYSTEM_SUCCESS.getMessage())
                .build();
    }

    /**
     * 成功信息返回 {@link GlobalServiceStatusCode#SYSTEM_SUCCESS}
     *
     * @param data 返回时带上的数据
     * @return 成功状态码以及数据
     */
    public static <T> Response<T> SYSTEM_SUCCESS(T data) {
        return Response.<T>builder()
                .code(GlobalServiceStatusCode.SYSTEM_SUCCESS.getCode())
                .info(GlobalServiceStatusCode.SYSTEM_SUCCESS.getMessage())
                .data(data)
                .build();
    }

    /**
     * 错误信息返回 {@link GlobalServiceStatusCode#SYSTEM_SERVICE_FAIL}
     *
     * @return Response
     */
    public static <T> Response<T> SYSTEM_FAIL() {
        return Response.<T>builder()
                .code(GlobalServiceStatusCode.SYSTEM_SERVICE_FAIL.getCode())
                .info(GlobalServiceStatusCode.SYSTEM_SERVICE_FAIL.getMessage())
                .build();
    }

    /**
     * 系统异常返回 {@link GlobalServiceStatusCode#SYSTEM_SERVICE_ERROR}
     *
     * @return Response
     */
    public static <T> Response<T> SERVICE_ERROR() {
        return Response.<T>builder()
                .code(GlobalServiceStatusCode.SYSTEM_SERVICE_ERROR.getCode())
                .info(GlobalServiceStatusCode.SYSTEM_SERVICE_ERROR.getMessage())
                .build();
    }

    /**
     * 系统异常返回, 自定义错误消息 {@link GlobalServiceStatusCode#SYSTEM_SERVICE_ERROR}
     *
     * @return Response
     */
    public static <T> Response<T> SERVICE_ERROR(String msg) {
        return Response.<T>builder()
                .code(GlobalServiceStatusCode.SYSTEM_SERVICE_ERROR.getCode())
                .info(Optional.ofNullable(msg).orElseGet(GlobalServiceStatusCode.SYSTEM_SERVICE_ERROR::getMessage))
                .build();
    }

    /**
     * 业务异常返回, 自定义code {@link GlobalServiceStatusCode#SYSTEM_SERVICE_ERROR}
     *
     * @param code 自定义状态码 {@link GlobalServiceStatusCode}
     * @return code对应的错误信息
     */
    public static <T> Response<T> CUSTOMIZE_ERROR(GlobalServiceStatusCode code) {
        return Response.<T>builder()
                .code(code.getCode())
                .info(code.getMessage())
                .build();
    }

    /**
     * 业务异常返回, 自定义code {@link GlobalServiceStatusCode#SYSTEM_SERVICE_ERROR}
     *
     * @param code 自定义状态码 {@link GlobalServiceStatusCode}
     * @param msg  自定义异常信息
     * @return code对应的错误信息
     */
    public static <T> Response<T> CUSTOMIZE_MSG_ERROR(GlobalServiceStatusCode code, String msg) {
        return Response.<T>builder()
                .code(code.getCode())
                .info(Optional.ofNullable(msg).orElseGet(code::getMessage))
                .build();
    }

    /**
     * 系统异常返回, 自定义错误 {@link GlobalServiceStatusCode#SYSTEM_SERVICE_ERROR}
     *
     * @param e APPException
     * @return code对应的错误信息
     */
    public static <T> Response<T> APP_EXCEPTION(AppException e) {
        return Response.<T>builder()
                .code(Integer.valueOf(e.getCode()))
                .info(e.getInfo())
                .build();
    }

    public static <T> Response<T> WX_EXCEPTION(WxException e) {
        return Response.<T>builder()
                .code(Integer.valueOf(e.getCode()))
                .info(e.getInfo())
                .build();
    }

    public static <T> Response<T> Ali_EXCEPTION(ClientException e) {
        return Response.<T>builder()
                .code(Integer.valueOf(e.getErrCode()))
                .info(e.getErrMsg())
                .build();
    }

    public static <T> Response<T> Ali_EXCEPTION(OSSException oe) {
        return Response.<T>builder()
                .code(OSS_DOWNLOAD_ERROR.getCode())
                .info(oe.getErrorMessage())
                .build();
    }

}
