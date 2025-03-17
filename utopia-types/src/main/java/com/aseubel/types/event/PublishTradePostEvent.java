package com.aseubel.types.event;

import com.aseubel.types.annotation.FieldDesc;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.aseubel.types.common.Constant.DATE_TIME_FORMATTER;

/**
 * @author Aseubel
 * @date 2025-03-13 09:32
 */
@Getter
@Setter
public class PublishTradePostEvent extends ApplicationEvent {

    @FieldDesc(name = "id")
    private Long id;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "价格")
    private double price;

    @FieldDesc(name = "标题")
    private String title;

    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "图片url")
    private String image;

    @FieldDesc(name = "类型,0-出售;1-求购:2-赠送")
    private int type;

    @FieldDesc(name = "学校代号")
    private String schoolCode;

    @FieldDesc(name = "发布时间")
    private LocalDateTime createTime;

    public PublishTradePostEvent(Object source) {
        super(source);
    }

    public PublishTradePostEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public PublishTradePostEvent(Object source, String userId, String postId) {
        super(source);
        this.userId = userId;
        this.postId = postId;
    }

    public PublishTradePostEvent(Object source, String userId, String postId, double price, String title, String content, String image, int type, String schoolCode) {
        super(source);
        this.userId = userId;
        this.postId = postId;
        this.price = price;
        this.title = title;
        this.content = content;
        this.image = image;
        this.type = type;
        this.schoolCode = schoolCode;
        this.createTime = LocalDateTime.now();
    }

    public Object getSource() {
        return super.getSource();
    }

    /**
     * 转换成json字符串
     */
    public String toJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[{\"userId\":\"").append(userId).append("\"")
                .append(", \"postId\":\"").append(postId).append("\"")
                .append(", \"title\":\"").append(title).append("\"")
                .append(", \"content\":\"").append(content).append("\"")
                .append(", \"image\":\"").append(image).append("\"")
                .append(", \"price\":\"").append(price).append("\"")
                .append(", \"type\":\"").append(type).append("\"")
                .append(", \"createTime\":\"").append(createTime.format(DATE_TIME_FORMATTER)).append("\"")
                .append("}]");
        return sb.toString();
    }
}
