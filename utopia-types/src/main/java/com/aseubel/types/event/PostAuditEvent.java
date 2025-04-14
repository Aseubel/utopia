package com.aseubel.types.event;

import com.aseubel.types.annotation.FieldDesc;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;
import java.util.List;

/**
 * @author Aseubel
 * @description 帖子审核事件
 * @date 2025/4/15 上午2:08
 */
@Getter
@Setter
public class PostAuditEvent extends ApplicationEvent {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "学校代码")
    private String schoolCode;

    @FieldDesc(name = "标题")
    private String title;

    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "图片urls")
    private List<String> imgUrls;

    public PostAuditEvent(Object source) {
        super(source);
    }

    public PostAuditEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public PostAuditEvent(Object source, String userId, String postId, String schoolCode, String title, String content, List<String> imgUrls) {
        super(source);
        this.userId = userId;
        this.postId = postId;
        this.schoolCode = schoolCode;
        this.title = title;
        this.content = content;
        this.imgUrls = imgUrls;
    }

    public Object getSource() {
        return super.getSource();
    }

}
