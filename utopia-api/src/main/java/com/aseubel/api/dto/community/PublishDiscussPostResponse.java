package com.aseubel.api.dto.community;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025-02-23 00:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublishDiscussPostResponse implements Serializable {

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "标题")
    private String title;

    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "发帖人id")
    private String userId;

    @FieldDesc(name = "标签")
    private String tag;

    @FieldDesc(name = "图片url")
    private String image;

}
