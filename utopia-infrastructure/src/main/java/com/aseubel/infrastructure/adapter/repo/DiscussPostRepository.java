package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.domain.user.model.entity.UserEntity;
import com.aseubel.infrastructure.convertor.CommunityImageConvertor;
import com.aseubel.infrastructure.convertor.DiscussPostConvertor;
import com.aseubel.infrastructure.dao.DiscussPostMapper;
import com.aseubel.infrastructure.dao.FavoriteMapper;
import com.aseubel.infrastructure.dao.ImageMapper;
import com.aseubel.infrastructure.dao.SchoolMapper;
import com.aseubel.infrastructure.dao.po.DiscussPost;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    @Resource
    private CommunityImageConvertor communityImageConvertor;

    @Resource
    private SchoolMapper schoolMapper;

    @Resource
    private FavoriteMapper favoriteMapper;

    @Override
    public List<DiscussPostEntity> listDiscussPost(String postId, Integer limit, String schoolCode) {
        return Optional.ofNullable(StringUtils.isEmpty(postId)
                        ? discussPostMapper.listDiscussPostAhead(limit, schoolCode)
                        : discussPostMapper.listDiscussPost(postId, limit, schoolCode))
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
        discussPostMapper.relateDiscussPostImage(postId, communityImageConvertor.convert(images, communityImageConvertor::convert));
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

    @Override
    public String querySchoolName(String schoolCode) {
        return Optional.ofNullable(schoolMapper.getSchoolNameBySchoolCode(schoolCode)).orElse("");
    }

    @Override
    public boolean isSchoolCodeValid(String schoolCode) {
        return Optional.ofNullable(schoolCode).map(schoolMapper::getSchoolBySchoolCode).isPresent();
    }

    @Override
    public List<DiscussPostEntity> queryUserFavoritePosts(String userId, String postId, Integer limit) {
        List<String> postIds = Optional.ofNullable(StringUtils.isEmpty(postId)
                        ? favoriteMapper.listUserFavoritePostIdAhead(userId, limit)
                        : favoriteMapper.listUserFavoritePostId(userId, postId, limit))
                .orElse(Collections.emptyList());
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyList();
        }
        return Optional.ofNullable(discussPostMapper.listDiscussPostByPostIds(postIds))
                .map(l -> {
                    Map<String, DiscussPostEntity> postMap = l.stream()
                            .map(discussPostConvertor::convert) // 先转换为 UserEntity
                            .collect(Collectors.toMap(DiscussPostEntity::getDiscussPostId, post -> post));
                    return postIds.stream()
                            .map(postMap::get)
                            .collect(Collectors.toList());
                })
                .orElse(Collections.emptyList());
    }

}
