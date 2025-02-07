package com.aseubel.types.event;

import com.aseubel.types.annotation.FieldDesc;
import com.aseubel.types.enums.CustomServiceCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author Aseubel
 * @description 自定义事件
 * @date 2025-02-07 14:36
 */
@Setter
@Getter
public class CustomEvent extends ApplicationEvent {

    @FieldDesc(name = "事件消息")
    private String message;

    @FieldDesc(name = "业务服务编号")
    private Integer code;

    public CustomEvent(Object source) {
        super(source);
    }

    public CustomEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public CustomEvent(Object source, String message, Integer code) {
        super(source);
        this.message = message;
        this.code = code;
    }

    public CustomEvent(Object source, CustomServiceCode customServiceCode) {
        super(source);
        this.message = customServiceCode.getMessage();
        this.code = customServiceCode.getCode();
    }

    public Object getSource() {
        return super.getSource();
    }
}
