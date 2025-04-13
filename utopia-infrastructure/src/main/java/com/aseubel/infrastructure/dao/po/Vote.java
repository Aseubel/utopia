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
public class Vote {

    @FieldDesc(name = "主键id")
    private long id;

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

    @FieldDesc(name = "投票时间")
    private LocalDateTime voteTime;

}
