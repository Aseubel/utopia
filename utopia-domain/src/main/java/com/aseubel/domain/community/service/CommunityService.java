package com.aseubel.domain.community.service;

import cn.hutool.core.collection.CollectionUtil;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.community.adapter.repo.ICommunityUserRepository;
import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.entity.CommentEntity;
import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.domain.user.model.entity.UserEntity;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.AliOSSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final ICommunityUserRepository communityUserRepository;

    private final AliOSSUtil aliOSSUtil;

    @Override
    public List<DiscussPostEntity> listDiscussPost(String postId, Integer limit, String schoolCode) {
        log.info("获取帖子列表服务开始执行");
        // 限制每页显示的帖子数量
        limit = limit == null ? PER_PAGE_DISCUSS_POST_SIZE : limit;
        // 查询帖子列表
        List<DiscussPostEntity> discussPostEntities = discussPostRepository.listDiscussPost(postId, limit);
        // 提取帖子的用户id
        List<String> userIds = Optional.ofNullable(discussPostEntities)
                .map(d -> d.stream()
                        .map(DiscussPostEntity::getUserId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        // 获取发帖人的用户名和头像，repo层已经保证了顺序
        List<UserEntity> users = CollectionUtil.isEmpty(userIds) ? Collections.emptyList() : communityUserRepository.queryUserBaseInfo(userIds);
        if (!CollectionUtil.isEmpty(discussPostEntities) && !CollectionUtil.isEmpty(users)) {
            for (int i = 0;i < discussPostEntities.size();i++) {
                discussPostEntities.get(i).setUserName(users.get(i).getUserName());
                discussPostEntities.get(i).setUserAvatar(users.get(i).getAvatar());
            }
        }
        // 获取帖子的第一张图片
        if (!CollectionUtil.isEmpty(discussPostEntities)) {
            discussPostEntities.forEach(d ->
                d.setImage(discussPostRepository.getPostFirstImage(d.getDiscussPostId()))
            );
        }
        log.info("获取帖子列表服务结束执行");
        return discussPostEntities;
    }

    @Override
    public CommunityImage uploadPostImage(CommunityImage postImage) throws ClientException {
        log.info("上传帖子图片服务开始执行，userId:{}", postImage.getUserId());
        if (ObjectUtils.isEmpty(communityUserRepository.queryUserStatus(postImage.getUserId()))) {
            throw new AppException("用户状态异常，请联系管理员");
        }
        // 上传图片到OSS
        postImage.generateImageId();
        String imageUrl = aliOSSUtil.upload(postImage.getImage(), postImage.getPostObjectName());
        postImage.setImageUrl(imageUrl);
        // 保存图片信息到数据库
        discussPostRepository.savePostImage(postImage);
        log.info("上传帖子图片服务结束执行，userId:{}", postImage.getUserId());
        return postImage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishDiscussPost(DiscussPostEntity discussPostEntity) {
        log.info("发布帖子服务开始执行，userId:{}", discussPostEntity.getUserId());
        checkUserStatus(discussPostEntity.getUserId());
        checkSchoolCodeValid(discussPostEntity.getSchoolCode());

        discussPostEntity.generatePostId();
        discussPostRepository.saveNewDiscussPost(discussPostEntity);

        if (!CollectionUtil.isEmpty(discussPostEntity.getImages())) {
            List<CommunityImage> images = discussPostRepository.listPostImagesByImageIds(discussPostEntity.getImages());
            if (!CollectionUtil.isEmpty(images)) {
                discussPostRepository.relateNewPostImage(discussPostEntity.getDiscussPostId(), images);
            }
        }

        log.info("发布帖子服务结束执行，userId:{}", discussPostEntity.getUserId());
    }

    @Override
    public DiscussPostEntity likeDiscussPost(String postId) {
        return null;
    }

    @Override
    public DiscussPostEntity commentDiscussPost(CommentEntity commentEntity) {
        return null;
    }

    @Override
    public DiscussPostEntity forwardDiscussPost(String postId) {
        return null;
    }

    @Override
    public DiscussPostEntity topDiscussPost(String postId) {
        return null;
    }

    private void checkSchoolCodeValid(String schoolCode) {
        if (StringUtils.isEmpty(schoolCode) || !discussPostRepository.isSchoolCodeValid(schoolCode)) {
            log.error("学校代号无效！, user={}", schoolCode);
            throw new AppException("学校代号无效！");
        }
    }

    private void checkUserStatus(String userId) {
        if (ObjectUtils.isEmpty(communityUserRepository.queryUserStatus(userId))) {
            log.error("用户状态异常，请联系管理员！, user={}", userId);
            throw new AppException("用户状态异常，请联系管理员！");
        }
    }
}
