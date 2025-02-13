package com.aseubel.domain.community.model.bo;

import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.domain.user.model.entity.UserEntity;
import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @description 数据传输实体类
 * @date 2025-02-12 23:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityBO {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "评论id")
    private String commentId;

    @FieldDesc(name = "帖子实体")
    private DiscussPostEntity postEntity;

}
