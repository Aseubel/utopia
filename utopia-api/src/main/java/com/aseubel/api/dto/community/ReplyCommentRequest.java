package com.aseubel.api.dto.community;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Aseubel
 * @description 规定回复评论的评论不带图片
 * @date 2025-02-16 13:52
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentRequest implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "回复评论id")
    private String replyTo;

    @FieldDesc(name = "根评论id")
    private String rootId;

    @FieldDesc(name = "评论内容")
    private String content;

}
