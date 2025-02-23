package com.aseubel.api.dto.community.post;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description 查看帖子详情请求参数
 * @date 2025-02-22 20:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryPostDetailRequest implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

}
