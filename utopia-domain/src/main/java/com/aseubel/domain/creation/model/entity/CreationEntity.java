package com.aseubel.domain.creation.model.entity;

import cn.hutool.core.util.IdUtil;
import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @date 2025-04-08 10:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreationEntity {

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

    public String getCreationId() {
        this.creationId = IdUtil.fastUUID();
        return this.creationId;
    }
}
