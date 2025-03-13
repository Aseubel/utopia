package com.aseubel.api.dto.community.comment;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description 查询子评论请求参数
 * @date 2025-02-23 20:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuerySubCommentRequest implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "根评论id")
    private String rootId;

    @FieldDesc(name = "上页最后的评论id")
    private String commentId;

    @FieldDesc(name = "每页显示数量")
    private Integer limit;

    @FieldDesc(name = "排序方式")
    private Integer sortType;

    @FieldDesc(name = "点赞数量")
    private Integer likeCount;

}
