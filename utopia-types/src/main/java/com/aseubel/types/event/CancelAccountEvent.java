package com.aseubel.types.event;

import com.aseubel.types.annotation.FieldDesc;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author Aseubel
 * @description 注销账号事件
 * @date 2025-02-18 10:27
 */
@Setter
@Getter
public class CancelAccountEvent extends ApplicationEvent {

    @FieldDesc(name = "用户id")
    private String userId;

    public CancelAccountEvent(Object source) {
        super(source);
    }

    public CancelAccountEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public Object getSource() {
        return super.getSource();
    }
}
