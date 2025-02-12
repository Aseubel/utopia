package com.aseubel.types.event;

import com.aseubel.types.annotation.FieldDesc;
import com.aseubel.types.enums.CustomServiceCode;
import lombok.*;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @description 点赞事件
 * @date 2025-02-12 23:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class LikeEvent extends ApplicationEvent {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "点赞时间")
    private LocalDateTime likeTime;

    @FieldDesc(name = "评论id")
    private String commentId;

    public LikeEvent(Object source) {
        super(source);
    }

    public LikeEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public Object getSource() {
        return super.getSource();
    }

}
