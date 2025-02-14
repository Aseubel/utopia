package com.aseubel.types.event;

import com.aseubel.types.annotation.FieldDesc;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author Aseubel
 * @description 收藏事件
 * @date 2025-02-14 20:27
 */
@Getter
@Setter
public class FavoriteEvent extends ApplicationEvent {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    public FavoriteEvent(Object source) {
        super(source);
    }

    public FavoriteEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public FavoriteEvent(Object source, String userId, String postId) {
        super(source);
        this.userId = userId;
        this.postId = postId;
    }

    public Object getSource() {
        return super.getSource();
    }

}
