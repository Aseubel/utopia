package com.aseubel.domain.community.listener;

import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.types.event.FavoriteEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.aseubel.types.common.Constant.FAVORITE_POST;

/**
 * @author Aseubel
 * @description 收藏事件监听器
 * @date 2025-02-14 20:32
 */
@Component
@Slf4j
public class FavoriteEventListener implements ApplicationListener<FavoriteEvent> {

    @Resource
    private IDiscussPostRepository discussPostRepository;

//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public void onApplicationEvent(FavoriteEvent event) {
//        CommunityBO communityBO = (CommunityBO) event.getSource();
//        if (discussPostRepository.favoritePost(communityBO.getUserId(), communityBO.getPostId())) {
//            discussPostRepository.increaseFavoriteCount(communityBO.getPostId());
//        } else {
//            discussPostRepository.decreaseFavoriteCount(communityBO.getPostId());
//        }
//    }

    @Override
    public void onApplicationEvent(FavoriteEvent event) {
        CommunityBO communityBO = (CommunityBO) event.getSource();
        discussPostRepository.userBehavior(communityBO.getUserId(), communityBO.getPostId(), FAVORITE_POST);
    }
}
