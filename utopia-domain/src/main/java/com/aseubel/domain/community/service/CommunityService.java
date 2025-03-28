package com.aseubel.domain.community.service;

import cn.hutool.core.collection.CollectionUtil;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.community.adapter.repo.ICommentRepository;
import com.aseubel.domain.community.adapter.repo.ICommunityUserRepository;
import com.aseubel.domain.community.adapter.repo.IDiscussPostRepository;
import com.aseubel.domain.community.adapter.repo.INoticeRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.domain.community.model.entity.CommentEntity;
import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.domain.community.model.entity.NoticeEntity;
import com.aseubel.domain.user.model.entity.UserEntity;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.AliOSSUtil;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
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

    private final INoticeRepository noticeRepository;

    private final AliOSSUtil aliOSSUtil;

    @Override
    public List<DiscussPostEntity> listDiscussPost(CommunityBO communityBO) {
        Integer limit = communityBO.getLimit();
        String schoolCode = communityBO.getSchoolCode();
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
                    d.setImage(discussPostRepository.getPostFirstImage(d.getPostId()));
                    // 加载评论
                    d.setComments(commentRepository.listPostMainComment(d.getPostId()));
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
        return discussPostEntities;
    }

    @Override
    public DiscussPostEntity getDiscussPost(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String postId = communityBO.getPostId();
        // 查询帖子
        DiscussPostEntity postEntity = discussPostRepository.getDiscussPost(communityBO);
        // 获取帖子的图片
        postEntity.setImages(discussPostRepository.listPostImages(postId));
        return postEntity;
    }

    @Override
    public CommunityImage uploadPostImage(CommunityImage postImage) throws ClientException {
        if (ObjectUtils.isEmpty(communityUserRepository.queryUserStatus(postImage.getUserId()))) {
            throw new AppException("用户状态异常，请联系管理员");
        }
        // 上传图片到OSS
        postImage.generateImageId();
        String imageUrl = aliOSSUtil.upload(postImage.getImage(), postImage.getPostObjectName());
        postImage.setImageUrl(imageUrl);
        // 保存图片信息到数据库
        discussPostRepository.savePostImage(postImage);
        return postImage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishDiscussPost(DiscussPostEntity post) {
        checkUserStatus(post.getUserId());
        checkSchoolCodeValid(post.getSchoolCode());

        post.setContent(SensitiveWordHelper.replace(post.getContent(), '*'));
        post.generatePostId();
        discussPostRepository.saveNewDiscussPost(post);

        if (!CollectionUtil.isEmpty(post.getImages())) {
            List<CommunityImage> images = discussPostRepository.listPostImagesByImageIds(post.getImages());
            if (!CollectionUtil.isEmpty(images)) {
                post.setImage(images.get(0).getImageUrl());
                discussPostRepository.relateNewPostImage(post.getPostId(), images);
            }
        }

    }

    @Override
    public List<DiscussPostEntity> queryUserFavoritePosts(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String postId = communityBO.getPostId();
        Integer limit = communityBO.getLimit();
        communityBO.setLimit(limit == null ? PER_PAGE_DISCUSS_POST_SIZE : limit);

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
        return discussPostEntities;
    }

    @Override
    public List<DiscussPostEntity> queryMyDiscussPosts(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String postId = communityBO.getPostId();
        Integer limit = communityBO.getLimit();
        communityBO.setLimit(limit == null ? PER_PAGE_DISCUSS_POST_SIZE : limit);

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
        return discussPostEntities;
    }

    @Override
    public void favoriteDiscussPost(String userId, String postId) {
        checkUserIdValid(userId);
        if (discussPostRepository.favoritePost(userId, postId)) {
            discussPostRepository.increaseFavoriteCount(postId);
        } else {
            discussPostRepository.decreaseFavoriteCount(postId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeDiscussPost(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String postId = communityBO.getPostId();

        if (discussPostRepository.likePost(userId, postId, communityBO.getEventTime())) {
            discussPostRepository.increaseLikeCount(postId);
        } else {
            discussPostRepository.decreaseLikeCount(postId);
        }
    }

    @Override
    public void topDiscussPost(String userId, String postId) {
        discussPostRepository.topPost(userId, postId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void commentDiscussPost(CommunityBO communityBO) {
        CommentEntity comment = communityBO.getCommentEntity();

        checkUserStatus(comment.getUserId());
        checkPostIdValid(comment.getPostId());

        comment.setContent(SensitiveWordHelper.replace(comment.getContent(), '*'));
        comment.generateCommentId();
        comment.setRootId(comment.getCommentId());
        commentRepository.saveRootComment(comment);

        if (!CollectionUtil.isEmpty(comment.getImages())) {
            List<CommunityImage> images = commentRepository.listCommentImagesByImageIds(comment.getImages());
            if (!CollectionUtil.isEmpty(images)) {
                commentRepository.relateNewCommentImage(comment.getCommentId(), images);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyComment(CommunityBO communityBO) {
        CommentEntity comment = communityBO.getCommentEntity();
        checkUserStatus(comment.getUserId());

        comment.setContent(SensitiveWordHelper.replace(comment.getContent(), '*'));
        comment.generateCommentId();
        commentRepository.saveReplyComment(comment);

        if (!CollectionUtil.isEmpty(comment.getImages())) {
            List<CommunityImage> images = commentRepository.listCommentImagesByImageIds(comment.getImages());
            if (!CollectionUtil.isEmpty(images)) {
                commentRepository.relateNewCommentImage(comment.getCommentId(), images);
            }
        }

    }

    @Override
    public CommunityImage uploadCommentImage(CommunityImage commentImage) throws ClientException {
        if (ObjectUtils.isEmpty(communityUserRepository.queryUserStatus(commentImage.getUserId()))) {
            throw new AppException("用户状态异常，请联系管理员");
        }
        // 上传图片到OSS
        commentImage.generateImageId();
        String imageUrl = aliOSSUtil.upload(commentImage.getImage(), commentImage.getPostObjectName());
        commentImage.setImageUrl(imageUrl);
        // 保存图片信息到数据库
        commentRepository.saveCommentImage(commentImage);
        return commentImage;
    }

    @Override
    public List<CommentEntity> listPostComment(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String postId = communityBO.getPostId();
        Integer limit = communityBO.getLimit();
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
        return comments;
    }

    @Override
    public List<CommentEntity> listSubComment(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String commentId = communityBO.getCommentId();
        Integer limit = communityBO.getLimit();
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
        return comments;
    }

    @Override
    public void likeComment(CommunityBO communityBO) {
        String userId = communityBO.getUserId();
        String commentId = communityBO.getCommentId();

        if (commentRepository.likeComment(communityBO)) {
            commentRepository.increaseLikeCount(commentId);
        } else {
            commentRepository.decreaseLikeCount(commentId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(CommunityBO communityBO) {
        discussPostRepository.deletePost(communityBO.getPostId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(CommunityBO communityBO) {
        // 顺序不能调换
        commentRepository.deleteComment(communityBO);
        commentRepository.decreaseRootCommentReplyCount(communityBO.getCommentId());
    }

    @Override
    public List<NoticeEntity> queryNotices(CommunityBO communityBO) {
        List<NoticeEntity> notices = noticeRepository.listNotices(communityBO);
        return notices;
    }

    @Override
    public void readNotice(CommunityBO communityBO) {
        noticeRepository.readNotice(communityBO);
    }

    @Override
    public void deleteNotice(CommunityBO communityBO) {
        noticeRepository.deleteNotice(communityBO);
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
