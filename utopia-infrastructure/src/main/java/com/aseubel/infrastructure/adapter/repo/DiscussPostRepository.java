package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.infrastructure.convertor.CommunityImageConvertor;
import com.aseubel.infrastructure.convertor.DiscussPostConvertor;
import com.aseubel.infrastructure.dao.DiscussPostMapper;
import com.aseubel.infrastructure.dao.ImageMapper;
import com.aseubel.infrastructure.dao.po.DiscussPost;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
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

    @Resource
    private ImageMapper imageMapper;
    @Autowired
    private CommunityImageConvertor communityImageConvertor;

    @Override
    public List<DiscussPostEntity> listDiscussPost(String postId, Integer limit) {
        return Optional.ofNullable(StringUtils.isEmpty(postId)
                        ? discussPostMapper.listDiscussPostAhead(limit)
                        : discussPostMapper.listDiscussPost(postId, limit))
                .map(p -> p.stream().map(discussPostConvertor::convert).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public void savePostImage(CommunityImage postImage) {
        imageMapper.addImage(communityImageConvertor.convert(postImage));
    }

    @Override
    public void saveNewDiscussPost(DiscussPostEntity discussPostEntity) {
        DiscussPost discussPost = discussPostConvertor.convert(discussPostEntity);
        discussPostMapper.addDiscussPost(discussPost);
    }

    @Override
    public void relateNewPostImage(String postId, List<CommunityImage> images) {
        discussPostMapper.relateDiscussPostImage(postId, images);
    }

    @Override
    public List<CommunityImage> listPostImagesByImageIds(List<String> imageIds) {
        return communityImageConvertor.convert(
                imageMapper.listImageByImageIds(imageIds), communityImageConvertor::convert);
    }

    @Override
    public String getPostFirstImage(String postId) {
        // 先获取第一张图片的id。再去image表中查询图片的url
        return Optional.ofNullable(discussPostMapper.getPostFirstImage(postId)).map(imageMapper::getImageUrl).orElse(null);
    }

}
