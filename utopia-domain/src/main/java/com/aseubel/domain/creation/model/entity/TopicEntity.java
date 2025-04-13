package com.aseubel.domain.creation.model.entity;

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
 * @date 2025/4/8 上午11:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicEntity {

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

    public void calculatePhrase() {
        long daysBetween = ChronoUnit.DAYS.between(START_CREATION_TIME.toLocalDate(), LocalDateTime.now().toLocalDate());
        this.phrase = (int) (daysBetween / 7) + 1; // 加 1 是因为我们希望从第一个星期开始计数
    }

    public String getTopicId() {
        this.topicId = IdUtil.fastUUID();
        return this.topicId;
    }

    @FieldDesc(name = "第一作品")
    private CreationEntity topCreation;
}
