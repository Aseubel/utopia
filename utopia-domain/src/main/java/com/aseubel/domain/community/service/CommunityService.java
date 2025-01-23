package com.aseubel.domain.community.service;

import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
        List<DiscussPostEntity> discussPostEntities = discussPostRepository.listDiscussPost(postId, limit);
        log.info("获取帖子列表服务结束执行");
        return discussPostEntities == null ? Collections.emptyList() : discussPostEntities;
    }

}
