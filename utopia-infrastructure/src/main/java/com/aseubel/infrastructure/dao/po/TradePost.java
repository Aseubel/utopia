package com.aseubel.infrastructure.dao.po;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @description 交易帖子持久化对象
 * @date 2025-01-30 22:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradePost {

    @FieldDesc(name = "id")
    private Long id;

    @FieldDesc(name = "帖子id")
    private String tradePostId;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "标题")
    private String title;

    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "价格")
    private Double price;

    @FieldDesc(name = "0-出售;1-求购:2-赠送")
    private Integer type;

    @FieldDesc(name = "0-未完成;1-已完成")
    private Integer status;

    @FieldDesc(name = "创建时间")
    private LocalDateTime createTime;

    @FieldDesc(name = "更新时间")
    private LocalDateTime updateTime;

}
