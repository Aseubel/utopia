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
public class DeletePostEvent extends ApplicationEvent {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    public DeletePostEvent(Object source) {
        super(source);
    }

    public DeletePostEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public Object getSource() {
        return super.getSource();
    }

}
