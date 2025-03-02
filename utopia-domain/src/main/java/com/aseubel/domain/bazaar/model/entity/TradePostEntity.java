package com.aseubel.domain.bazaar.model.entity;

import com.aseubel.domain.community.model.entity.CommentEntity;
import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Aseubel
 * @description 集市交易帖子实体类
 * @date 2025-01-30 22:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradePostEntity {

    @FieldDesc(name = "帖子id")
    private String tradePostId;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "用户昵称")
    private String userName;

    @FieldDesc(name = "用户头像")
    private String userAvatar;

    @FieldDesc(name = "标题")
    private String title;

    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "价格")
    private Double price;

    @FieldDesc(name = "联系方式")
    private String contact;

    @FieldDesc(name = "学校代码")
    private String schoolCode;

    @FieldDesc(name = "0-出售;1-求购:2-赠送")
    private Integer type;

    @FieldDesc(name = "0-未完成;1-已完成")
    private Integer status;

    @FieldDesc(name = "标签")
    private List<String> tags;

    @FieldDesc(name = "创建时间")
    private LocalDateTime createTime;

    @FieldDesc(name = "更新时间")
    private LocalDateTime updateTime;

    @FieldDesc(name = "图片list")
    private List<String> images;

    @FieldDesc(name = "第一张图片")
    private String image;

    public void generatePostId() {
        this.tradePostId = "tp_" + UUID.randomUUID().toString().replaceAll("-", "");
    }

}
