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
 * @date 2025-03-11 23:09
 */
@Getter
@Setter
public class PublishDiscussPostEvent extends ApplicationEvent {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

    @FieldDesc(name = "标题")
    private String title;

    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "图片url")
    private String image;

    @FieldDesc(name = "标签")
    private String tag;

    @FieldDesc(name = "点赞数")
    private int likeCount;

    @FieldDesc(name = "评论数")
    private int commentCount;

    @FieldDesc(name = "收藏数")
    private int favoriteCount;

    @FieldDesc(name = "学校代码")
    private String schoolCode;

    @FieldDesc(name = "发布时间")
    private LocalDateTime createTime;

    public PublishDiscussPostEvent(Object source) {
        super(source);
    }

    public PublishDiscussPostEvent(Object source, Clock clock) {
        super(source, clock);
    }

    public PublishDiscussPostEvent(Object source, String userId, String postId) {
        super(source);
        this.userId = userId;
        this.postId = postId;
    }

    public PublishDiscussPostEvent(Object source, String userId, String postId, String title, String content, String image, String tag, String schoolCode) {
        super(source);
        this.userId = userId;
        this.content = content;
        this.title = title;
        this.image = image;
        this.postId = postId;
        this.tag = tag;
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
        sb.append("[{")
                .append("\"userId\":\"").append(userId).append("\"")
                .append(", \"postId\":\"").append(postId).append("\"")
                .append(", \"title\":\"").append(title).append("\"")
                .append(", \"content\":\"").append(content).append("\"")
                .append(", \"image\":\"").append(image).append("\"")
                .append(", \"tag\":\"").append(tag).append("\"")
                .append(", \"likeCount\":\"").append(likeCount).append("\"")
                .append(", \"commentCount\":\"").append(commentCount).append("\"")
                .append(", \"favoriteCount\":\"").append(favoriteCount).append("\"")
                .append(", \"createTime\":\"").append(createTime.format(DATE_TIME_FORMATTER)).append("\"")
                .append("}]");
        return sb.toString();
    }
}
