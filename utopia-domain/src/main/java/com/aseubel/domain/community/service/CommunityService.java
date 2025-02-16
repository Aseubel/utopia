package com.aseubel.domain.community.service;

import cn.hutool.core.collection.CollectionUtil;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.community.adapter.repo.ICommentRepository;
import com.aseubel.domain.community.adapter.repo.ICommunityUserRepository;
import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
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

    private final ICommentRepository commentRepository;

    private final AliOSSUtil aliOSSUtil;

    @Override
    public List<DiscussPostEntity> listDiscussPost(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String postId = communityBO.getPostId();
        Integer limit = communityBO.getLimit();
        String schoolCode = communityBO.getSchoolCode();
        log.info("获取帖子列表服务开始执行");
        checkUserIdValid(userId);
        checkSchoolCodeValid(schoolCode);
        // 限制每页显示的帖子数量
        limit = limit == null ? PER_PAGE_DISCUSS_POST_SIZE : limit;
        // 查询帖子列表
        List<DiscussPostEntity> discussPostEntities = discussPostRepository.listDiscussPost(communityBO);
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
            discussPostEntities.forEach(d -> {
                d.setImage(discussPostRepository.getPostFirstImage(d.getDiscussPostId()));
                // 加载评论
                d.setComments(commentRepository.listPostMainComment(d.getDiscussPostId()));
                    }
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
    public List<DiscussPostEntity> queryUserFavoritePosts(String userId, String postId, Integer limit) {
        log.info("查询用户收藏帖子服务开始执行，userId:{}", userId);
        checkUserIdValid(userId);
        // 限制每页显示的帖子数量
        limit = limit == null ? PER_PAGE_DISCUSS_POST_SIZE : limit;
        // 查询帖子列表
        List<DiscussPostEntity> discussPostEntities = discussPostRepository.queryUserFavoritePosts(userId, postId, limit);
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
        log.info("查询用户收藏帖子服务结束执行，userId:{}", userId);
        return discussPostEntities;
    }

    @Override
    public void favoriteDiscussPost(String userId, String postId) {
        log.info("用户收藏帖子服务开始，userId: {}, postId: {}", userId, postId);
        checkUserIdValid(userId);
        discussPostRepository.favoritePost(userId, postId);
        log.info("用户收藏帖子服务开始，userId: {}, postId: {}", userId, postId);
    }

    @Override
    public void likeDiscussPost(CommunityBO communityBO) {
        log.info("用户点赞帖子服务开始，userId: {}, postId: {}", communityBO.getUserId(), communityBO.getPostId());
        discussPostRepository.likePost(communityBO.getUserId(), communityBO.getPostId(), communityBO.getEventTime());
        log.info("用户点赞帖子服务结束，userId: {}, postId: {}", communityBO.getUserId(), communityBO.getPostId());
    }

    @Override
    public void topDiscussPost(String userId,String postId) {
        discussPostRepository.topPost(userId, postId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void commentDiscussPost(CommunityBO communityBO) {
        CommentEntity commentEntity = communityBO.getCommentEntity();
        log.info("评论帖子服务开始执行，userId:{}", commentEntity.getUserId());
        checkUserStatus(commentEntity.getUserId());

        commentEntity.generateCommentId();
        commentRepository.saveRootComment(commentEntity);

        if (!CollectionUtil.isEmpty(commentEntity.getImages())) {
            List<CommunityImage> images = commentRepository.listCommentImagesByImageIds(commentEntity.getImages());
            if (!CollectionUtil.isEmpty(images)) {
                commentRepository.relateNewCommentImage(commentEntity.getCommentId(), images);
            }
        }

        log.info("评论帖子服务结束执行，userId:{}", commentEntity.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyComment(CommunityBO communityBO) {
        CommentEntity commentEntity = communityBO.getCommentEntity();
        log.info("回复评论服务开始执行，userId:{}", commentEntity.getUserId());
        checkUserStatus(commentEntity.getUserId());

        commentEntity.generateCommentId();
        commentRepository.saveReplyComment(commentEntity);

        if (!CollectionUtil.isEmpty(commentEntity.getImages())) {
            List<CommunityImage> images = commentRepository.listCommentImagesByImageIds(commentEntity.getImages());
            if (!CollectionUtil.isEmpty(images)) {
                commentRepository.relateNewCommentImage(commentEntity.getCommentId(), images);
            }
        }

        log.info("回复评论服务结束执行，userId:{}", commentEntity.getUserId());
    }

    @Override
    public CommunityImage uploadCommentImage(CommunityImage commentImage) throws ClientException {
        log.info("上传评论图片服务开始执行，userId:{}", commentImage.getUserId());
        if (ObjectUtils.isEmpty(communityUserRepository.queryUserStatus(commentImage.getUserId()))) {
            throw new AppException("用户状态异常，请联系管理员");
        }
        // 上传图片到OSS
        commentImage.generateImageId();
        String imageUrl = aliOSSUtil.upload(commentImage.getImage(), commentImage.getPostObjectName());
        commentImage.setImageUrl(imageUrl);
        // 保存图片信息到数据库
        commentRepository.saveCommentImage(commentImage);
        log.info("上传帖子评论服务结束执行，userId:{}", commentImage.getUserId());
        return commentImage;
    }

    private void checkSchoolCodeValid(String schoolCode) {
        if (StringUtils.isEmpty(schoolCode) || !discussPostRepository.isSchoolCodeValid(schoolCode)) {
            log.error("学校代号无效！, user={}", schoolCode);
            throw new AppException("学校代号无效！");
        }
    }

    private void checkUserStatus(String userId) {
        if (StringUtils.isEmpty(userId) || ObjectUtils.isEmpty(communityUserRepository.queryUserStatus(userId))) {
            log.error("用户状态异常，请联系管理员！, user={}", userId);
            throw new AppException("用户状态异常，请联系管理员！");
        }
    }

    private void checkUserIdValid(String userId) {
        if (StringUtils.isEmpty(userId) || ObjectUtils.isEmpty(communityUserRepository.queryUserStatus(userId))) {
            log.error("用户id无效！, user={}", userId);
            throw new AppException("用户id无效！");
        }
    }
}
