package com.aseubel.types.event;

import com.aseubel.types.annotation.FieldDesc;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author Aseubel
 * @description 删除评论事件
 * @date 2025-02-28 16:34
 */
@Setter
@Getter
public class DeleteCommentEvent extends ApplicationEvent {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "评论帖子id")
    private String commentId;

    public DeleteCommentEvent(Object source) {
        super(source);
    }

    public DeleteCommentEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public Object getSource() {
        return super.getSource();
    }

}
