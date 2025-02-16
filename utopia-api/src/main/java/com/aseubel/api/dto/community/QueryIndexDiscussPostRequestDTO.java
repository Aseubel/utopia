package com.aseubel.api.dto.community;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Aseubel
 * @description 查询首页帖子请求DTO
 * @date 2025-01-23 19:18
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryIndexDiscussPostRequestDTO implements Serializable {

    @FieldDesc(name = "用户id")
    @NotNull(message = "用户id不能为空")
    private String userId;

    @NotNull(message = "学校代号不能为空")
    @FieldDesc(name = "所属院校代号")
    private String schoolCode;

    @FieldDesc(name = "上一页查询最后一条记录的id")
    private String postId;

    @Max(value = 50, message = "每页记录数不能超过50")
    @Min(value = 1, message = "每页记录数不能小于1")
    @FieldDesc(name = "每页记录数")
    private Integer limit;

    @FieldDesc(name = "标签")
    private String tag;

}
