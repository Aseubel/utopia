package com.aseubel.infrastructure.adapter.repo;

import cn.hutool.core.collection.CollectionUtil;
import com.aseubel.domain.community.adapter.repo.INoticeRepository;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.domain.community.model.entity.NoticeEntity;
import com.aseubel.infrastructure.convertor.DiscussPostConvertor;
import com.aseubel.infrastructure.convertor.NoticeConvertor;
import com.aseubel.infrastructure.dao.*;
import com.aseubel.infrastructure.dao.po.Comment;
import com.aseubel.infrastructure.dao.po.DiscussPost;
import com.aseubel.infrastructure.dao.po.Notice;
import com.aseubel.infrastructure.dao.po.User;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.aseubel.types.common.Constant.NOTICE_TYPE_COMMENT_POST;
import static com.aseubel.types.common.Constant.NOTICE_TYPE_COMMENT_REPLY;

/**
 * @author Aseubel
 * @date 2025-03-18 20:40
 */
@Repository
public class NoticeRepository implements INoticeRepository {

    @Resource
    private NoticeMapper noticeMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private DiscussPostMapper discussPostMapper;

    @Resource
    private ImageMapper imageMapper;

    @Resource
    private NoticeConvertor noticeConvertor;

    @Resource
    private DiscussPostConvertor discussPostConvertor;

    @Override
    public List<NoticeEntity> listNotices(CommunityBO communityBO) {
        DiscussPostRepository discussPostRepository = new DiscussPostRepository();
        List<NoticeEntity> notices = noticeConvertor.convert(
                noticeMapper.listNotices(communityBO.getUserId(), communityBO.getNoticeId(), communityBO.getLimit()), noticeConvertor::convert
        );

        if (CollectionUtil.isEmpty(notices)) {
            return List.of();
        }

        Set<String> userIds = notices.stream()
                .flatMap(n -> Stream.of(n.getReceiverId(), n.getUserId()))
                .collect(Collectors.toSet());

        Set<String> postIds = notices.stream()
                .map(NoticeEntity::getPostId)
                .collect(Collectors.toSet());

        Set<String> commentIds = notices.stream()
                .flatMap(n -> Stream.of(n.getCommentId(), n.getMyCommentId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 批量查询
        Map<String, User> userMap = userMapper.listUserBaseInfoByUserIds(new ArrayList<>(userIds))
                .stream()
                .collect(Collectors.toMap(User::getUserId, Function.identity()));

        Map<String, String> commentContentMap = commentMapper.listCommentContentByCommentIds(new ArrayList<>(commentIds))
                .stream()
                .collect(Collectors.toMap(Comment::getCommentId, Comment::getContent));

        Map<String, DiscussPost> postEntityMap = discussPostMapper.listDiscussPostByPostIds(new ArrayList<>(postIds))
                .stream()
                .collect(Collectors.toMap(DiscussPost::getDiscussPostId, Function.identity()));

        // 预加载图片
        Map<String, String> postImageMap = postIds.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        this::getPostFirstImage
                ));

        notices.forEach(n -> {
            Optional.ofNullable(userMap.get(n.getUserId())).ifPresent(user -> {
                n.setUserName(user.getUserName());
                n.setUserAvatar(user.getAvatar());
            });

            Optional.ofNullable(postEntityMap.get(n.getPostId())).ifPresent(post -> {
                n.setPostTitle(post.getTitle());
                n.setSchoolCode(post.getSchoolCode());
                n.setPostImage(Optional.ofNullable(postImageMap.get(n.getPostId())).orElse(""));
                Optional.ofNullable(userMap.get(post.getUserId()))
                        .ifPresent(pUser -> n.setPostUserName(pUser.getUserName()));
            });

            n.setCommentContent(commentContentMap.getOrDefault(n.getCommentId(), ""));
            n.setMyCommentContent(commentContentMap.getOrDefault(n.getMyCommentId(), ""));
        });
        return notices;
    }

    @Override
    public void addNotice(CommunityBO bo, int type) {
        if (type == NOTICE_TYPE_COMMENT_POST) {
            String receiverId = discussPostMapper.getUserIdByPostId(bo.getPostId());
            if (receiverId.equals(bo.getUserId())) {
                return;
            }
            NoticeEntity notice = NoticeEntity.builder()
                    .userId(bo.getUserId())
                    .postId(bo.getPostId())
                    .commentId(bo.getCommentId())
                    .receiverId(receiverId)
                    .type(NOTICE_TYPE_COMMENT_POST)
                    .build();
            notice.generateId();
            noticeMapper.InsertNoticeComment(noticeConvertor.convert(notice));
        } else if (type == NOTICE_TYPE_COMMENT_REPLY) {
            String receiverId = commentMapper.getUserIdByCommentId(bo.getReplyTo());
            if (receiverId.equals(bo.getUserId())) {
                return;
            }
            NoticeEntity notice = NoticeEntity.builder()
                    .userId(bo.getUserId())
                    .receiverId(receiverId)
                    .postId(bo.getPostId())
                    .commentId(bo.getCommentId())
                    .myCommentId(bo.getReplyTo())
                    .type(NOTICE_TYPE_COMMENT_REPLY)
                    .build();
            notice.generateId();
            noticeMapper.insertNoticeReply(noticeConvertor.convert(notice));
        }
    }

    @Override
    public void readNotice(CommunityBO communityBO) {
        noticeMapper.readNotice(communityBO.getUserId(), communityBO.getEventTime());
    }

    @Override
    public void deleteNotice(CommunityBO communityBO) {
        noticeMapper.deleteNotices(communityBO.getUserId(), communityBO.getEventTime());
    }

    private String getPostFirstImage(String postId) {
        return Optional.ofNullable(discussPostMapper.getPostFirstImage(postId)).map(imageMapper::getImageUrl).orElse("");
    }

}
