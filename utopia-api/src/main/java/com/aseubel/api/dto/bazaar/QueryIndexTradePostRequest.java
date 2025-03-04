package com.aseubel.api.dto.bazaar;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Aseubel
 * @description 查询集市首页帖子请求DTO
 * @date 2025-01-30 23:50
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryIndexTradePostRequest implements Serializable {

    @FieldDesc(name = "用户id")
    @NotNull(message = "用户id不能为空")
    private String userId;

    @FieldDesc(name = "上一页查询最后一条记录的id")
    private String postId;

    @Max(value = 50, message = "每页记录数不能超过50")
    @Min(value = 1, message = "每页记录数不能小于1")
    @FieldDesc(name = "每页记录数")
    private Integer limit;

    @FieldDesc(name = "类型")
    private Integer type;

    @FieldDesc(name = "状态")
    private Integer status;

    @FieldDesc(name = "学校代码")
    private String schoolCode;
    
}
