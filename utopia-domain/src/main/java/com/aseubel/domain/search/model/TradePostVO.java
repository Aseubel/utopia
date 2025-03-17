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
public class TradePostVO {

    @FieldDesc(name = "id")
    private Long id;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "标题")
    private String title;

    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "价格")
    private double price;

    @FieldDesc(name = "学校代号")
    private String schoolCode;

    @FieldDesc(name = "类型,0-出售;1-求购:2-赠送")
    private int type;

    @FieldDesc(name = "状态")
    private int status;

    @FieldDesc(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @FieldDesc(name = "is_deleted")
    private int isDeleted;

    @FieldDesc(name = "图片url")
    private String image;

    /**
     * 从binlog获取数据
     */
    public static TradePostVO parseFrom(Serializable[] afterRowData) {
        List<Serializable> list = Arrays.stream(afterRowData).toList();
        TradePostVO vo = new TradePostVO();
        vo.setId((Long) list.get(0));
        vo.setPostId((String) list.get(1));
        vo.setUserId((String) list.get(2));
        vo.setTitle((String) list.get(3));
        vo.setContent(StrUtil.str(list.get(4), StandardCharsets.UTF_8));
        vo.setPrice((Float) list.get(5));
        vo.setSchoolCode((String) list.get(7));
        vo.setType((Integer) list.get(8));
        vo.setStatus((Integer) list.get(9));
        vo.setCreateTime(((Date)list.get(10)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        vo.setIsDeleted((Integer) list.get(12));
        return vo;
    }

    /**
     * 从canal的afterColumns中解析出TradePostVO对象
     */
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

    public static TradePostVO parseFromBinlog(Map<String, Serializable> rowData) {
        return null;
    }

    /**
     * 转换成json字符串
     */
    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[{\"userId\":\"").append(userId).append("\"")
                .append(", \"postId\":\"").append(postId).append("\"")
                .append(", \"title\":\"").append(title).append("\"")
                .append(", \"content\":\"").append(content).append("\"")
                .append(", \"image\":\"").append(image).append("\"")
                .append(", \"price\":\"").append(price).append("\"")
                .append(", \"type\":\"").append(type).append("\"")
                .append(", \"createTime\":\"").append(createTime.format(DATE_TIME_FORMATTER)).append("\"")
                .append("}]");
        return sb.toString();
    }

    /**
     * 转换成json字符串
     */
    public String toJsonStringWithoutImage() {
        StringBuilder sb = new StringBuilder();
        sb.append("[{\"userId\":\"").append(userId).append("\"")
                .append(", \"postId\":\"").append(postId).append("\"")
                .append(", \"title\":\"").append(title).append("\"")
                .append(", \"content\":\"").append(content).append("\"")
                .append(", \"price\":\"").append(price).append("\"")
                .append(", \"type\":\"").append(type).append("\"")
                .append(", \"createTime\":\"").append(createTime.format(DATE_TIME_FORMATTER)).append("\"")
                .append("}]");
        return sb.toString();
    }

}
