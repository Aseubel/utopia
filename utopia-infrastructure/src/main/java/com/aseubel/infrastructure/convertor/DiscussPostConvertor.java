package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import com.aseubel.infrastructure.dao.po.DiscussPost;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Aseubel
 * @description 讨论帖子实体类转换器
 * @date 2025-01-22 13:38
 */
@Component
public class DiscussPostConvertor {

    public DiscussPostEntity convert(DiscussPost discussPost) {
        return DiscussPostEntity.builder()
                .discussPostId(discussPost.getDiscussPostId())
                .userId(discussPost.getUserId())
                .schoolCode(discussPost.getSchoolCode())
                .title(discussPost.getTitle())
                .content(discussPost.getContent())
                .likeCount(discussPost.getLikeCount())
                .commentCount(discussPost.getCommentCount())
                .favoriteCount(discussPost.getFavoriteCount())
                .type(discussPost.getType())
                .status(discussPost.getStatus())
                .createTime(discussPost.getCreateTime())
                .updateTime(discussPost.getUpdateTime())
                .build();
    }

    public DiscussPost convert(DiscussPostEntity discussPostEntity) {
        return DiscussPost.builder()
                .discussPostId(discussPostEntity.getDiscussPostId())
                .userId(discussPostEntity.getUserId())
                .schoolCode(discussPostEntity.getSchoolCode())
                .title(discussPostEntity.getTitle())
                .content(discussPostEntity.getContent())
                .likeCount(discussPostEntity.getLikeCount())
                .commentCount(discussPostEntity.getCommentCount())
                .favoriteCount(discussPostEntity.getFavoriteCount())
                .type(discussPostEntity.getType())
                .status(discussPostEntity.getStatus())
                .createTime(discussPostEntity.getCreateTime())
                .updateTime(discussPostEntity.getUpdateTime())
                .build();
    }

    public <T, U> List<U> convert(List<T> items, Function<T, U> converter) {
        return items.stream().map(converter).collect(Collectors.toList());
    }


}
