package com.aseubel.types.event;

import com.aseubel.types.annotation.FieldDesc;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @description 点赞事件
 * @date 2025-02-12 23:51
 */
@Getter
@Setter
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

    public LikeEvent(Object source, String userId, String postId) {
        super(source);
        this.userId = userId;
        this.postId = postId;
    }

    public LikeEvent(Object source, String userId, String postId, LocalDateTime likeTime, String commentId) {
        super(source);
        this.userId = userId;
        this.postId = postId;
        this.likeTime = likeTime;
        this.commentId = commentId;
    }

    public Object getSource() {
        return super.getSource();
    }

}
