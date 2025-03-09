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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.aseubel.types.common.Constant.*;

/**
 * @author Aseubel
 * @description 社区服务实现类
 * @date 2025-01-23 19:11
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommunityService implements ICommunityService {

    private final IDiscussPostRepository discussPostRepository;

    private final ICommunityUserRepository communityUserRepository;

    private final ICommentRepository commentRepository;

    private final AliOSSUtil aliOSSUtil;

    @Override
    public List<DiscussPostEntity> listDiscussPost(CommunityBO communityBO) {
        Integer limit = communityBO.getLimit();
        String schoolCode = communityBO.getSchoolCode();
        log.info("获取帖子列表服务开始执行");
        checkSchoolCodeValid(schoolCode);
        // 限制每页显示的帖子数量
        communityBO.setLimit(limit == null ? PER_PAGE_DISCUSS_POST_SIZE : limit);
        // 查询帖子列表
        List<DiscussPostEntity> discussPostEntities = discussPostRepository.listDiscussPost(communityBO);
        if (CollectionUtil.isEmpty(discussPostEntities)) {
            return Collections.emptyList();
        }
        // 提取帖子的用户id
        List<String> userIds = Optional.of(discussPostEntities)
                .map(d -> d.stream()
                        .map(DiscussPostEntity::getUserId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        // 获取发帖人的用户名和头像
        Map<String, UserEntity> users = CollectionUtil.isEmpty(userIds) ? Collections.emptyMap() : communityUserRepository.queryUserBaseInfo(userIds);
        if (CollectionUtil.isNotEmpty(users)) {
            discussPostEntities.forEach(d -> {
                d.setUserName(users.get(d.getUserId()).getUserName());
                d.setUserAvatar(users.get(d.getUserId()).getAvatar());
            });
        }

        discussPostEntities.forEach(d -> {
                    // 获取帖子的第一张图片
                    d.setImage(discussPostRepository.getPostFirstImage(d.getDiscussPostId()));
                    // 加载评论
                    d.setComments(commentRepository.listPostMainComment(d.getDiscussPostId()));
                    // 获取主评论的用户名
                    if (!CollectionUtil.isEmpty(d.getComments())) {
                        Map<String, String> userNameMap = communityUserRepository.queryUserNames(d.getComments().stream()
                                .map(CommentEntity::getUserId)
                                .collect(Collectors.toList()));
                        if (!CollectionUtil.isEmpty(userNameMap)) {
                            d.getComments().forEach(comment ->
                                    comment.setUserName(userNameMap.get(comment.getUserId()))
                            );
                        }
                    }
                }
        );
        log.info("获取帖子列表服务结束执行");
        return discussPostEntities;
    }

    @Override
    public DiscussPostEntity getDiscussPost(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String postId = communityBO.getPostId();
        log.info("获取帖子详情服务开始执行, userId:{}, postId:{}", userId, postId);
        // 查询帖子
        DiscussPostEntity postEntity = discussPostRepository.getDiscussPost(communityBO);
        // 获取帖子的图片
        postEntity.setImages(discussPostRepository.listPostImages(postId));
        log.info("获取帖子详情服务结束执行, userId:{}, postId:{}", userId, postId);
        return postEntity;
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
    public List<DiscussPostEntity> queryUserFavoritePosts(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String postId = communityBO.getPostId();
        Integer limit = communityBO.getLimit();
        communityBO.setLimit(limit == null ? PER_PAGE_DISCUSS_POST_SIZE : limit);

        log.info("查询用户收藏帖子服务开始执行，userId:{}", userId);
        checkUserIdValid(userId);
        // 查询帖子列表
        List<DiscussPostEntity> discussPostEntities = discussPostRepository.queryUserFavoritePosts(communityBO);
        if (CollectionUtil.isEmpty(discussPostEntities)) {
            return Collections.emptyList();
        }
        // 提取帖子的用户id
        List<String> userIds = Optional.of(discussPostEntities)
                .map(d -> d.stream()
                        .map(DiscussPostEntity::getUserId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        // 获取发帖人的用户名和头像
        Map<String, UserEntity> users = CollectionUtil.isEmpty(userIds) ? Collections.emptyMap() : communityUserRepository.queryUserBaseInfo(userIds);
        if (CollectionUtil.isNotEmpty(users)) {
            discussPostEntities.forEach(d -> {
                d.setUserName(users.get(d.getUserId()).getUserName());
                d.setUserAvatar(users.get(d.getUserId()).getAvatar());
            });
        }
        log.info("查询用户收藏帖子服务结束执行，userId:{}", userId);
        return discussPostEntities;
    }

    @Override
    public List<DiscussPostEntity> queryMyDiscussPosts(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String postId = communityBO.getPostId();
        Integer limit = communityBO.getLimit();
        communityBO.setLimit(limit == null ? PER_PAGE_DISCUSS_POST_SIZE : limit);

        log.info("查询用户发布的讨论帖子服务开始执行，userId:{}", userId);
        checkUserIdValid(userId);
        // 查询帖子列表
        List<DiscussPostEntity> discussPostEntities = discussPostRepository.queryUserDiscussPosts(communityBO);
        if (CollectionUtil.isEmpty(discussPostEntities)) {
            return Collections.emptyList();
        }
        // 提取帖子的用户id
        List<String> userIds = Optional.of(discussPostEntities)
                .map(d -> d.stream()
                        .map(DiscussPostEntity::getUserId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        // 获取发帖人的用户名和头像
        Map<String, UserEntity> users = CollectionUtil.isEmpty(userIds) ? Collections.emptyMap() : communityUserRepository.queryUserBaseInfo(userIds);
        if (CollectionUtil.isNotEmpty(users)) {
            discussPostEntities.forEach(d -> {
                d.setUserName(users.get(d.getUserId()).getUserName());
                d.setUserAvatar(users.get(d.getUserId()).getAvatar());
            });
        }
        log.info("查询用户发布的讨论帖子服务结束执行，userId:{}", userId);
        return discussPostEntities;
    }

    @Override
    public void favoriteDiscussPost(String userId, String postId) {
        log.info("用户收藏帖子服务开始，userId: {}, postId: {}", userId, postId);
        checkUserIdValid(userId);
        if (discussPostRepository.favoritePost(userId, postId)) {
            discussPostRepository.increaseFavoriteCount(postId);
        } else {
            discussPostRepository.decreaseFavoriteCount(postId);
        }
        log.info("用户收藏帖子服务开始，userId: {}, postId: {}", userId, postId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeDiscussPost(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String postId = communityBO.getPostId();

        log.info("用户点赞帖子服务开始，userId: {}, postId: {}", userId, postId);
        if (discussPostRepository.likePost(userId, postId, communityBO.getEventTime())) {
            discussPostRepository.increaseLikeCount(postId);
        } else {
            discussPostRepository.decreaseLikeCount(postId);
        }
        log.info("用户点赞帖子服务结束，userId: {}, postId: {}", userId, communityBO.getPostId());
    }

    @Override
    public void topDiscussPost(String userId, String postId) {
        discussPostRepository.topPost(userId, postId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void commentDiscussPost(CommunityBO communityBO) {
        CommentEntity commentEntity = communityBO.getCommentEntity();
        log.info("评论帖子服务开始执行，userId:{}", commentEntity.getUserId());
        checkUserStatus(commentEntity.getUserId());
        checkPostIdValid(commentEntity.getPostId());

        commentEntity.generateCommentId();
        commentEntity.setRootId(commentEntity.getCommentId());
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

    @Override
    public List<CommentEntity> listPostComment(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String postId = communityBO.getPostId();
        Integer limit = communityBO.getLimit();
        log.info("获取帖子评论列表服务开始执行, userId:{}, postId:{}", userId, postId);
        // 限制每页显示的帖子数量
        communityBO.setLimit(limit == null ? PER_PAGE_COMMENT_SIZE : limit);
        // 查询评论列表
        List<CommentEntity> comments = commentRepository.listPostComment(communityBO);
        if (CollectionUtil.isEmpty(comments)) {
            return Collections.emptyList();
        }
        for (CommentEntity comment : comments) {
            if (CollectionUtil.isNotEmpty(comment.getReplyList())) {
                Map<String ,String> cmtIdUserNameMap = communityUserRepository.queryUserNamesByCommentIds(comment.getReplyList().stream()
                        .map(CommentEntity::getReplyTo)
                        .collect(Collectors.toList()));
                for (CommentEntity reply : comment.getReplyList()) {
                    reply.setReplyToName(cmtIdUserNameMap.get(reply.getReplyTo()));
                }
            }
        }
        // 提取评论的用户id
        List<String> userIds = Optional.of(comments)
                .map(d -> d.stream()
                        .flatMap(comment -> Stream.concat(
                                Stream.of(comment.getUserId()),
                                comment.getReplyList().stream().map(CommentEntity::getUserId)
                        ))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        // 获取评论人的用户名和头像
        Map<String, UserEntity> users = CollectionUtil.isEmpty(userIds) ? Collections.emptyMap() : communityUserRepository.queryUserBaseInfo(userIds);
        if (CollectionUtil.isNotEmpty(users)) {
            comments.forEach(c -> {
                c.setUserName(users.get(c.getUserId()).getUserName());
                c.setUserAvatar(users.get(c.getUserId()).getAvatar());
                if (CollectionUtil.isNotEmpty(c.getReplyList())) {
                    c.getReplyList().forEach(reply -> {
                        reply.setUserName(users.get(reply.getUserId()).getUserName());
                        reply.setUserAvatar(users.get(reply.getUserId()).getAvatar());
                    });
                }
            });
        }
        // 获取评论的图片
        if (!CollectionUtil.isEmpty(comments)) {
            comments.forEach(d -> d.setImages(commentRepository.listCommentImages(d.getCommentId())));
        }
        log.info("获取帖子评论列表服务结束执行, userId:{}, postId:{}", userId, postId);
        return comments;
    }

    @Override
    public List<CommentEntity> listSubComment(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String commentId = communityBO.getCommentId();
        Integer limit = communityBO.getLimit();
        log.info("获取子评论服务开始执行, userId:{}, commentId:{}", userId, commentId);
        // 限制每页显示的帖子数量
        communityBO.setLimit(limit == null ? PER_PAGE_SUB_COMMENT_SIZE : limit);
        // 查询评论列表
        List<CommentEntity> comments = commentRepository.listSubComment(communityBO);
        if (CollectionUtil.isEmpty(comments)) {
            return Collections.emptyList();
        }
        Map<String ,String> userNamesMap = communityUserRepository.queryUserNamesByCommentIds(comments.stream()
                .map(CommentEntity::getReplyTo)
                .collect(Collectors.toList()));
        for (CommentEntity comment : comments) {
            if (!CollectionUtil.isEmpty(userNamesMap)) {
                comment.setReplyToName(userNamesMap.get(comment.getReplyTo()));
            }
        }
        // 提取评论的用户id
        List<String> userIds = Optional.of(comments)
                .map(d -> d.stream()
                        .map(CommentEntity::getUserId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        // 获取评论人的用户名和头像，repo层已经保证了顺序
        Map<String, UserEntity> users = CollectionUtil.isEmpty(userIds) ? Collections.emptyMap() : communityUserRepository.queryUserBaseInfo(userIds);
        if (CollectionUtil.isNotEmpty(users)) {
            comments.forEach(c -> {
                c.setUserName(users.get(c.getUserId()).getUserName());
                c.setUserAvatar(users.get(c.getUserId()).getAvatar());
            });
        }
        log.info("获取子评论服务结束执行, userId:{}, commentId:{}", userId, commentId);
        return comments;
    }

    @Override
    public void likeComment(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String commentId = communityBO.getCommentId();

        log.info("用户点赞评论服务开始，userId: {}, commentId: {}", userId, commentId);
        if (commentRepository.likeComment(userId, commentId, communityBO.getEventTime())) {
            commentRepository.increaseLikeCount(commentId);
        } else {
            commentRepository.decreaseLikeCount(commentId);
        }
        log.info("用户点赞评论服务结束，userId: {}, commentId: {}", userId, commentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(CommunityBO communityBO) {
        verifyPostAuth(communityBO.getUserId(), communityBO.getPostId());
        discussPostRepository.deletePost(communityBO.getPostId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(CommunityBO communityBO) {
        verifyCommentAuth(communityBO.getUserId(), communityBO.getCommentId());
        // 顺序不能调换
        commentRepository.deleteComment(communityBO);
        commentRepository.decreaseRootCommentReplyCount(communityBO.getCommentId());
    }

    private void verifyPostAuth(String userId, String postId) {
        if (!userId.equals(discussPostRepository.getUserIdByPostId(postId))) {
            log.error("用户无权删除帖子！, user={}, post={}", userId, postId);
            throw new AppException("用户无权删除帖子！");
        }
    }

    private void verifyCommentAuth(String userId, String commentId) {
        if (!userId.equals(commentRepository.getUserIdByCommentId(commentId))) {
            log.error("用户无权删除评论！, user={}, comment={}", userId, commentId);
            throw new AppException("用户无权删除评论！");
        }
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

    private void checkPostIdValid(String postId) {
        if (StringUtils.isEmpty(discussPostRepository.getUserIdByPostId(postId))) {
            log.error("帖子id无效！, post={}", postId);
            throw new AppException("帖子id无效！");
        }
    }
}
