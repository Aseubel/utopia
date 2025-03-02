package com.aseubel.api.dto.bazaar;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @description 查询集市首页帖子列表返回DTO
 * @date 2025-01-30 23:52
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryIndexTradePostResponse implements Serializable {

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

    @FieldDesc(name = "0-出售;1-求购:2-赠送")
    private Integer type;

    @FieldDesc(name = "0-未完成;1-已完成")
    private Integer status;

    @FieldDesc(name = "创建时间")
    private LocalDateTime createTime;

    @FieldDesc(name = "更新时间")
    private LocalDateTime updateTime;

    @FieldDesc(name = "图片url，因为展示在首页，所以只展示第一张图片")
    private String image;

}
