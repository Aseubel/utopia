package com.aseubel.types.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author Aseubel
 * @description 回复评论事件
 * @date 2025-02-16 16:07
 */
@Setter
@Getter
public class ReplyCommentEvent extends ApplicationEvent {
    public ReplyCommentEvent(Object source) {
        super(source);
    }

    public ReplyCommentEvent(Object source, Clock clock) {
        super(source, clock);
    }
}
