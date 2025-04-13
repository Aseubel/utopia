package com.aseubel.infrastructure.dao.po;

import cn.hutool.core.util.IdUtil;
import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.aseubel.types.common.Constant.START_CREATION_TIME;

/**
 * @author Aseubel
 * @date 2025-04-08 10:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Topic {

    @FieldDesc(name = "主键id")
    private long id;

    @FieldDesc(name = "话题ID")
    private String topicId;

    @FieldDesc(name = "名称")
    private String topicName;

    @FieldDesc(name = "描述")
    private String topicDesc;

    @FieldDesc(name = "第几期")
    private int phrase;

    @FieldDesc(name = "类型，0-文字")
    private int type;

    @FieldDesc(name = "示例")
    private String example;

    @FieldDesc(name = "状态，0-候选；1-已选")
    private int status;

    @FieldDesc(name = "票数")
    private int voteCount;

    @FieldDesc(name = "创建时间")
    private LocalDateTime createTime;

    @FieldDesc(name = "更新时间")
    private LocalDateTime updateTime;

}
