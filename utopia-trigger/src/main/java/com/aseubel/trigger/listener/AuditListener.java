package com.aseubel.trigger.listener;

import com.aseubel.domain.bazaar.model.bo.BazaarBO;
import com.aseubel.domain.bazaar.model.entity.TradePostEntity;
import com.aseubel.domain.bazaar.service.IBazaarService;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.domain.community.service.ICommunityService;
import com.aseubel.types.event.DeleteDiscussPostEvent;
import com.aseubel.types.event.PostAuditEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Aseubel
 * @date 2025/4/15 上午2:12
 */
@Component
@RequiredArgsConstructor
public class AuditListener implements ApplicationListener<PostAuditEvent> {

    private final ICommunityService communityService;
    private final IBazaarService bazaarService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void onApplicationEvent(PostAuditEvent event) {
        if (event.getSource().toString().equals("communityPostAudit")) {
            DiscussPostEntity post = DiscussPostEntity.builder()
                    .postId(event.getPostId())
                    .userId(event.getUserId())
                    .schoolCode(event.getSchoolCode())
                    .title(event.getTitle())
                    .content(event.getContent())
                    .images(event.getImgUrls())
                    .build();
            boolean isPass = communityService.auditPost(post);
            if (!isPass) {
                CommunityBO communityBO = CommunityBO.builder()
                        .postEntity(post)
                        .postId(event.getPostId())
                        .userId(event.getUserId())
                        .schoolCode(event.getSchoolCode())
                        .build();
                communityService.deletePost(communityBO);
                eventPublisher.publishEvent(new DeleteDiscussPostEvent("deleteDiscussPost", event.getUserId(), event.getPostId(), event.getSchoolCode()));
            }
        } else if (event.getSource().toString().equals("bazaarCommentAudit")) {
            TradePostEntity post = TradePostEntity.builder()
                    .postId(event.getPostId())
                    .userId(event.getUserId())
                    .schoolCode(event.getSchoolCode())
                    .title(event.getTitle())
                    .content(event.getContent())
                    .images(event.getImgUrls())
                    .build();
            boolean isPass = bazaarService.auditPost(post);
            if (!isPass) {
                BazaarBO bazaarBO = BazaarBO.builder()
                        .userId(event.getUserId())
                        .postId(event.getPostId())
                        .schoolCode(event.getSchoolCode())
                        .build();
                bazaarService.deletePost(bazaarBO);
            }
        }

    }
}
