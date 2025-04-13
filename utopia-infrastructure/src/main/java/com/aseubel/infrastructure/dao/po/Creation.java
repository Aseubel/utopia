package com.aseubel.infrastructure.dao.po;

import cn.hutool.core.util.IdUtil;
import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @date 2025-04-08 10:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Creation {

    @FieldDesc(name = "主键id")
    private long id;

    @FieldDesc(name = "作品id")
    private String creationId;

    @FieldDesc(name = "话题id")
    private String topicId;

    @FieldDesc(name = "排名")
    private Integer rank;

    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "票数")
    private int voteCount;

    @FieldDesc(name = "创建时间")
    private LocalDateTime createTime;

    @FieldDesc(name = "更新时间")
    private LocalDateTime updateTime;

}
