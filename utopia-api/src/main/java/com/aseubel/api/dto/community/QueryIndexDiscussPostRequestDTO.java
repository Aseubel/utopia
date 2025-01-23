package com.aseubel.api.dto.community;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

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
    private String userId;

    @FieldDesc(name = "上一页查询最后一条记录的id")
    private String postId;

    @FieldDesc(name = "每页记录数")
    private Integer limit;

}
