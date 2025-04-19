package com.aseubel.domain.community.model.vo;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @date 2025/4/17 下午5:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Score {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "点赞数")
    private int likeCount;

    @FieldDesc(name = "收藏数")
    private int favoriteCount;

    @FieldDesc(name = "评论数")
    private int commentCount;

    @FieldDesc(name = "ces得分")
    private Double cesScore;

    public Double getCesScore() {
        return likeCount * 2 + favoriteCount * 4 + commentCount * 2 * 1.0;
    }

}
