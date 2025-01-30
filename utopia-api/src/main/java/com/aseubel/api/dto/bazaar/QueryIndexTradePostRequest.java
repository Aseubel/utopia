package com.aseubel.api.dto.bazaar;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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

}
