package com.aseubel.domain.search.model;

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
public class TradePostVO {

    @FieldDesc(name = "id")
    private Long id;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "价格")
    private double price;

    @FieldDesc(name = "标题")
    private String title;

    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "图片url")
    private String image;

    @FieldDesc(name = "类型,0-出售;1-求购:2-赠送")
    private int type;

    @FieldDesc(name = "学校代号")
    private String schoolCode;

    @FieldDesc(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @FieldDesc(name = "is_deleted")
    private int isDeleted;

    @FieldDesc(name = "状态")
    private int status;

    public static TradePostVO parseFrom(List<CanalEntry.Column> afterColumns) {
        TradePostVO vo = new TradePostVO();
        Map<String, String> columnMap = afterColumns.stream()
               .collect(Collectors.toMap(CanalEntry.Column::getName, CanalEntry.Column::getValue));

        // 处理基本属性
        Optional.ofNullable(columnMap.get("id"))
               .map(Long::parseLong)
               .ifPresent(vo::setId);
        Optional.ofNullable(columnMap.get("user_id"))
               .ifPresent(vo::setUserId);
        Optional.ofNullable(columnMap.get("trade_post_id"))
               .ifPresent(vo::setPostId);
        Optional.ofNullable(columnMap.get("price"))
               .map(Double::parseDouble)
               .ifPresent(vo::setPrice);
        Optional.ofNullable(columnMap.get("title"))
               .ifPresent(vo::setTitle);
        Optional.ofNullable(columnMap.get("content"))
               .ifPresent(vo::setContent);
        Optional.ofNullable(columnMap.get("image"))
               .ifPresent(vo::setImage);
        Optional.ofNullable(columnMap.get("type"))
               .map(Integer::parseInt)
               .ifPresent(vo::setType);
        Optional.ofNullable(columnMap.get("school_code"))
               .ifPresent(vo::setSchoolCode);
        Optional.ofNullable(columnMap.get("is_deleted"))
               .map(Integer::parseInt)
               .ifPresent(vo::setIsDeleted);
        Optional.ofNullable(columnMap.get("status"))
               .map(Integer::parseInt)
               .ifPresent(vo::setStatus);

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
                ", \"price\":\"" + price + '\"' +
                ", \"type\":\"" + type + '\"' +
                ", \"createTime\":\"" + createTime + '\"' +
                "}]";
    }

}
