package com.aseubel.types.event;

import com.aseubel.types.annotation.FieldDesc;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author Aseubel
 * @description 删除帖子事件
 * @date 2025-02-28 12:47
 */
@Setter
@Getter
public class DeleteDiscussPostEvent extends ApplicationEvent {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "学校代码")
    private String schoolCode;

    public DeleteDiscussPostEvent(Object source) {
        super(source);
    }

    public DeleteDiscussPostEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public DeleteDiscussPostEvent(Object source, String userId, String postId, String schoolCode) {
        super(source);
        this.userId = userId;
        this.postId = postId;
        this.schoolCode = schoolCode;
    }

    public Object getSource() {
        return super.getSource();
    }

}
