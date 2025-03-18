package com.aseubel.domain.community.listener.notice;

import com.aseubel.domain.community.adapter.repo.INoticeRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.types.event.ReplyCommentEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.aseubel.types.common.Constant.NOTICE_TYPE_COMMENT_REPLY;

/**
 * @author Aseubel
 * @description 回复评论事件监听器，发通知
 * @date 2025-03-18 22:35
 */
@Component
@Slf4j
public class NoticeReplyCommentListener implements ApplicationListener<ReplyCommentEvent> {

    @Resource
    private INoticeRepository noticeRepository;

    @Override
    public void onApplicationEvent(ReplyCommentEvent event) {
        CommunityBO bo = (CommunityBO) event.getSource();
        noticeRepository.addNotice(bo, NOTICE_TYPE_COMMENT_REPLY);
    }
}
