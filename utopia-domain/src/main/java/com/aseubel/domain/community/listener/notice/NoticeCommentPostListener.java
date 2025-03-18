package com.aseubel.domain.community.listener.notice;

import com.aseubel.domain.community.adapter.repo.INoticeRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.types.event.CommentPostEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.aseubel.types.common.Constant.NOTICE_TYPE_COMMENT_POST;

/**
 * @author Aseubel
 * @description 评论帖子事件监听器，发通知
 * @date 2025-03-18 21:50
 */
@Component
@Slf4j
public class NoticeCommentPostListener implements ApplicationListener<CommentPostEvent> {

    @Resource
    private INoticeRepository noticeRepository;

    @Override
    public void onApplicationEvent(CommentPostEvent event) {
        CommunityBO bo = (CommunityBO) event.getSource();
        noticeRepository.addNotice(bo, NOTICE_TYPE_COMMENT_POST);
    }

}
