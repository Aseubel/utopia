package com.aseubel.types.event;

import com.aseubel.types.annotation.FieldDesc;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author Aseubel
 * @description 完成交易事件
 * @date 2025-03-13 12:24
 */
@Setter
@Getter
public class CompleteTradeEvent extends ApplicationEvent {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "学校代码")
    private String schoolCode;

    public CompleteTradeEvent(Object source) {
        super(source);
    }

    public CompleteTradeEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public CompleteTradeEvent(Object source, String userId, String postId, String schoolCode) {
        super(source);
        this.userId = userId;
        this.postId = postId;
        this.schoolCode = schoolCode;
    }

    public Object getSource() {
        return super.getSource();
    }

}
