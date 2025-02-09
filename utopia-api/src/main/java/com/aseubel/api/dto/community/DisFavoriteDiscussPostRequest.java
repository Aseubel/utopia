package com.aseubel.api.dto.community;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description 取消收藏帖子请求
 * @date 2025-02-09 11:43
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisFavoriteDiscussPostRequest implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

}
