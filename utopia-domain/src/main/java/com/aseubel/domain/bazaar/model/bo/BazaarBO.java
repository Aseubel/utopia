package com.aseubel.domain.bazaar.model.bo;

import com.aseubel.domain.bazaar.model.entity.TradePostEntity;
import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

/**
 * @author Aseubel
 * @description 集市数据传输类
 * @date 2025-02-16 19:18
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BazaarBO {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "每页记录数")
    private Integer limit;

    @FieldDesc(name = "类型")
    private Integer type;

    @FieldDesc(name = "状态")
    private Integer status;

    @FieldDesc(name = "帖子实体")
    private TradePostEntity tradePostEntity;

    @FieldDesc(name = "学校代码")
    private String schoolCode;

}
