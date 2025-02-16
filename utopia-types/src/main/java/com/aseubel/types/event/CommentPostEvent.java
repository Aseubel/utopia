package com.aseubel.types.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author Aseubel
 * @description 评论帖子事件
 * @date 2025-02-16 13:59
 */
@Setter
@Getter
public class CommentPostEvent extends ApplicationEvent {
    public CommentPostEvent(Object source) {
        super(source);
    }

    public CommentPostEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
