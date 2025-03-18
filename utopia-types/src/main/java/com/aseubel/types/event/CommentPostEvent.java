package com.aseubel.types.event;

import com.aseubel.types.annotation.FieldDesc;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "评论内容")
    private String content;

    public Object getSource() {
        return super.getSource();
    }

    public CommentPostEvent(Object source) {
        super(source);
    }

    public CommentPostEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public CommentPostEvent(Object source, String postId) {
        super(source);
        this.postId = postId;
    }
}
