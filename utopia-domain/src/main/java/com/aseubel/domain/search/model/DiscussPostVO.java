package com.aseubel.domain.search.model;

import cn.hutool.core.util.StrUtil;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.aseubel.types.annotation.FieldDesc;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.aseubel.types.common.Constant.DATE_TIME_FORMATTER;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @FieldDesc(name = "is_deleted")
    private int isDeleted;

    /**
     * 适用于从binlog获取的数据
     */
    public static DiscussPostVO parseFrom(Serializable[] afterRowData) {
        List<Serializable> list = Arrays.stream(afterRowData).toList();
        DiscussPostVO vo = new DiscussPostVO();
        vo.setId((Long) list.get(0));
        vo.setPostId((String) list.get(1));
        vo.setUserId((String) list.get(2));
        vo.setSchoolCode((String) list.get(3));
        vo.setTitle((String) list.get(4));
        vo.setContent(StrUtil.str(list.get(5), StandardCharsets.UTF_8));
        vo.setTag((String) list.get(6));
        vo.setLikeCount((Integer) list.get(7));
        vo.setFavoriteCount((Integer) list.get(8));
        vo.setCommentCount((Integer) list.get(9));
        vo.setCreateTime(((Date)list.get(12)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        vo.setIsDeleted((Integer) list.get(14));
        return vo;
    }

    /**
     * 适用于从canal获取的数据
     */
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
        StringBuilder sb = new StringBuilder();
        sb.append("[{")
                .append("\"userId\":\"").append(userId).append("\"")
                .append(", \"postId\":\"").append(postId).append("\"")
                .append(", \"title\":\"").append(title).append("\"")
                .append(", \"content\":\"").append(content).append("\"")
                .append(", \"image\":\"").append(image).append("\"")
                .append(", \"tag\":\"").append(tag).append("\"")
                .append(", \"likeCount\":\"").append(likeCount).append("\"")
                .append(", \"commentCount\":\"").append(commentCount).append("\"")
                .append(", \"favoriteCount\":\"").append(favoriteCount).append("\"")
                .append(", \"createTime\":\"").append(createTime.format(DATE_TIME_FORMATTER)).append("\"")
                .append("}]");
        return sb.toString();
    }

    /**
     * 转换成json字符串
     */
    public String toJsonStringWithoutImage() {
        StringBuilder sb = new StringBuilder();
        sb.append("[{")
                .append("\"userId\":\"").append(userId).append("\"")
                .append(", \"postId\":\"").append(postId).append("\"")
                .append(", \"title\":\"").append(title).append("\"")
                .append(", \"content\":\"").append(content).append("\"")
                .append(", \"tag\":\"").append(tag).append("\"")
                .append(", \"likeCount\":\"").append(likeCount).append("\"")
                .append(", \"commentCount\":\"").append(commentCount).append("\"")
                .append(", \"favoriteCount\":\"").append(favoriteCount).append("\"")
                .append(", \"createTime\":\"").append(createTime.format(DATE_TIME_FORMATTER)).append("\"")
                .append("}]");
        return sb.toString();
    }

}
