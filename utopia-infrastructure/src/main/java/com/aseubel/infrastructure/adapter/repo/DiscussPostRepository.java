package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.infrastructure.convertor.DiscussPostConvertor;
import com.aseubel.infrastructure.dao.DiscussPostMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Aseubel
 * @description 讨论帖子仓储层实现类
 * @date 2025-01-22 13:35
 */
@Repository
public class DiscussPostRepository implements IDiscussPostRepository {

    @Resource
    private DiscussPostMapper discussPostMapper;

    @Resource
    private DiscussPostConvertor discussPostConvertor;

    @Override
    public List<DiscussPostEntity> listDiscussPost(String postId, Integer limit) {
        return Optional.ofNullable(StringUtils.isEmpty(postId)
                        ? discussPostMapper.listDiscussPostAhead(limit)
                        : discussPostMapper.listDiscussPost(postId, limit))
                .map(p -> p.stream().map(discussPostConvertor::convert).collect(Collectors.toList()))
                .orElse(null);
    }

}
