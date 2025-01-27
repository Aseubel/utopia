package com.aseubel.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author chensongmin
 * @description 全局服务响应状态码枚举
 * @date 2024/11/11
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum GlobalServiceStatusCode {
    /* 成功, 默认200 */
    SYSTEM_SUCCESS(200, "操作成功"),

    /* 系统错误 负数 */
    SYSTEM_SERVICE_FAIL(-4396, "操作失败"),
    SYSTEM_SERVICE_ERROR(-500, "系统异常"),
    SYSTEM_TIME_OUT(-1, "请求超时"),

    /* 参数错误：1001～2000 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),
    PARAM_FAILED_VALIDATE(1005, "参数未通过验证"),

    REQUEST_NOT_VALID(1101, "请求无效"),

    /* 用户模块错误：2001～3000 */
    USER_TOKEN_EXPIRED(2001, "token失效"),
    USER_TOKEN_ERROR(2002, "token错误"),
    USER_NOT_LOGIN(2003, "用户未登录"),
    USER_NOT_EXIST(2004, "用户不存在"),

    /* 微信模块错误 */
    WX_USER_CODE_INVALID(40029, "js_code无效"),
    WX_API_CALL_FREQUENTLY(45011, "API调用太频繁，请稍候再试"),
    WX_CODE_BLOCKED(40226, "高风险等级用户"),

    /* 阿里云OSS模块错误 */
    OSS_CONFIG_ERROR(50000, "OSS配置错误"),
    OSS_UPLOAD_ERROR(50001, "OSS上传失败"),
    OSS_DOWNLOAD_ERROR(50002, "OSS下载失败"),
    OSS_DELETE_ERROR(50003, "OSS删除失败"),
    OSS_BUCKET_NOT_EXIST(50004, "OSS桶不存在"),
    OSS_OBJECT_NOT_EXIST(50005, "OSS对象不存在"),

    ;

    private Integer code;
    private String message;

}
