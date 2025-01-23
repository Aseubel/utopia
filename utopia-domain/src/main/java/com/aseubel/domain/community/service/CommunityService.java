package com.aseubel.domain.community.service;

import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.aseubel.types.common.Constant.PER_PAGE_DISCUSS_POST_SIZE;

/**
 * @author Aseubel
 * @description 社区服务实现类
 * @date 2025-01-23 19:11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService implements ICommunityService{

    private final IDiscussPostRepository discussPostRepository;

    @Override
    public List<DiscussPostEntity> listDiscussPost(String postId, Integer limit) {
        log.info("获取帖子列表服务开始执行");
        limit = limit == null ? PER_PAGE_DISCUSS_POST_SIZE : limit;
        List<DiscussPostEntity> discussPostEntities = discussPostRepository.listDiscussPost(postId, limit);
        log.info("获取帖子列表服务结束执行");
        return discussPostEntities == null ? Collections.emptyList() : discussPostEntities;
    }

}
