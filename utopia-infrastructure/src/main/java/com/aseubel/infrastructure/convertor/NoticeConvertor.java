package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.community.model.entity.NoticeEntity;
import com.aseubel.infrastructure.dao.po.Notice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Aseubel
 * @date 2025-03-18 20:43
 */
@Component
public class NoticeConvertor {

    public NoticeEntity convert(Notice notice) {
        return NoticeEntity.builder()
                .noticeId(notice.getNoticeId())
                .userId(notice.getUserId())
                .receiverId(notice.getReceiverId())
                .postId(notice.getPostId())
                .commentId(notice.getCommentId())
                .myCommentId(notice.getMyCommentId())
                .type(notice.getType())
                .status(notice.getStatus())
                .time(notice.getCreateTime())
                .build();
    }

    public Notice convert(NoticeEntity noticeEntity) {
        return Notice.builder()
                .noticeId(noticeEntity.getNoticeId())
                .userId(noticeEntity.getUserId())
                .receiverId(noticeEntity.getReceiverId())
                .postId(noticeEntity.getPostId())
                .commentId(noticeEntity.getCommentId())
                .myCommentId(noticeEntity.getMyCommentId())
                .type(noticeEntity.getType())
                .status(noticeEntity.getStatus())
                .createTime(noticeEntity.getTime())
                .build();
    }

    public <T, U> List<U> convert(List<T> items, Function<T, U> converter) {
        return items.stream().map(converter).collect(Collectors.toList());
    }

}
