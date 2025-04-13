package com.aseubel.domain.creation.model.entity;

import cn.hutool.core.util.IdUtil;
import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @date 2025/4/8 上午11:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteEntity {

    @FieldDesc(name = "投票ID")
    private String voteId;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "话题id")
    private String topicId;

    @FieldDesc(name = "作品id")
    private String creationId;

    @FieldDesc(name = "投票类型, 0-投话题;1-投作品")
    private int type;

    public String getVoteId() {
        this.voteId = IdUtil.objectId();
        return this.voteId;
    }
}
