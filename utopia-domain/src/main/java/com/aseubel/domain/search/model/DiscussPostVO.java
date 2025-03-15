package com.aseubel.domain.search.model;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Aseubel
 * @date 2025-03-12 11:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscussPostVO {

    @FieldDesc(name = "id")
    private Long id;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "标题")
    private String title;

    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "图片url")
    private String image;

    @FieldDesc(name = "标签")
    private String tag;

    @FieldDesc(name = "点赞数")
    private int likeCount;

    @FieldDesc(name = "评论数")
    private int commentCount;

    @FieldDesc(name = "收藏数")
    private int favoriteCount;

    @FieldDesc(name = "学校代号")
    private String schoolCode;

    @FieldDesc(name = "创建时间")
    private LocalDateTime createTime;

    @FieldDesc(name = "is_deleted")
    private int isDeleted;

    public static DiscussPostVO parseFrom(List<CanalEntry.Column> afterColumns) {
        DiscussPostVO vo = new DiscussPostVO();
        Map<String, String> columnMap = afterColumns.stream()
                .collect(Collectors.toMap(
                        CanalEntry.Column::getName,
                        CanalEntry.Column::getValue,
                        (v1, v2) -> v2));

        // 使用Optional处理可能为null的字段值
        Optional.ofNullable(columnMap.get("user_id")).ifPresent(vo::setUserId);
        Optional.ofNullable(columnMap.get("discuss_post_id")).ifPresent(vo::setPostId);
        Optional.ofNullable(columnMap.get("title")).ifPresent(vo::setTitle);
        Optional.ofNullable(columnMap.get("content")).ifPresent(vo::setContent);
        Optional.ofNullable(columnMap.get("image")).ifPresent(vo::setImage);
        Optional.ofNullable(columnMap.get("tag")).ifPresent(vo::setTag);
        Optional.ofNullable(columnMap.get("school_code")).ifPresent(vo::setSchoolCode);
        Optional.ofNullable(columnMap.get("is_deleted")).map(Integer::parseInt).ifPresent(vo::setIsDeleted);

        // 处理数值类型转换
        Optional.ofNullable(columnMap.get("like_count"))
                .map(Integer::parseInt).ifPresent(vo::setLikeCount);
        Optional.ofNullable(columnMap.get("comment_count"))
                .map(Integer::parseInt).ifPresent(vo::setCommentCount);
        Optional.ofNullable(columnMap.get("favorite_count"))
                .map(Integer::parseInt).ifPresent(vo::setFavoriteCount);

        // 处理日期转换（需要与数据库格式一致）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Optional.ofNullable(columnMap.get("create_time"))
                .map(time -> LocalDateTime.parse(time, formatter))
                .ifPresent(vo::setCreateTime);

        return vo;
    }


    /**
     * 转换成json字符串
     */
    public String toJsonString() {
        return "[{" +
                "\"userId\":\"" + userId + '\"' +
                ", \"postId\":\"" + postId + '\"' +
                ", \"title\":\"" + title + '\"' +
                ", \"content\":\"" + content + '\"' +
                ", \"image\":\"" + image + '\"' +
                ", \"tag\":\"" + tag + '\"' +
                ", \"likeCount\":" + likeCount +
                ", \"commentCount\":" + commentCount +
                ", \"favoriteCount\":" + favoriteCount +
                ", \"createTime\":\"" + createTime + '\"' +
                "}]";
    }

}
