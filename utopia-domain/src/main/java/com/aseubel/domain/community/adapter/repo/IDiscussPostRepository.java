package com.aseubel.domain.community.adapter.repo;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.community.model.bo.CommunityBO;
import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aseubel
 * @description 帖子仓储层接口
 * @date 2025-01-23 19:08
 */
@Repository
public interface IDiscussPostRepository {

    /**
     * 直接列举帖子
     * @return
     */
    List<DiscussPostEntity> listDiscussPost(CommunityBO communityBO);

    /**
     * 根据帖子id获取帖子
     * @return
     */
    DiscussPostEntity getDiscussPost(CommunityBO communityBO);

    /**
     * 根据帖子id获取用户id
     */
    String getUserIdByPostId(String postId);

    /**
     * 保存帖子图片记录
     * @param postImage
     */
    void savePostImage(CommunityImage postImage);

    /**
     * 保存新帖子
     * @param discussPostEntity
     */
    void saveNewDiscussPost(DiscussPostEntity discussPostEntity);

    /**
     * 关联帖子图片
     * @param postId
     * @param images
     */
    void relateNewPostImage(String postId, List<CommunityImage> images);

    /**
     * 根据帖子id列举帖子
     * @param imageIds
     * @return
     */
    List<CommunityImage> listPostImagesByImageIds(List<String> imageIds);

    /**
     * 根据帖子id获取第一张图片
     * @param postId
     * @return
     */
    String getPostFirstImage(String postId);

    /**
     * 根据帖子id列举帖子图片
     * @param postId
     * @return
     */
    List<String> listPostImages(String postId);

    /**
     * 根据院校代号查询院校名称
     * @param schoolCode
     * @return
     */
    String querySchoolName(String schoolCode);

    /**
     * 判断院校代号是否有效
     * @param schoolCode 院校代号
     * @return
     */
    boolean isSchoolCodeValid(String schoolCode);

    /**
     * 根据用户id和帖子id分页查询用户收藏的帖子
     * @return
     */
    List<DiscussPostEntity> queryUserFavoritePosts(CommunityBO communityBO);

    /**
     * 根据用户id和帖子id分页查询用户发布的帖子
     * @return
     */
    List<DiscussPostEntity> queryUserDiscussPosts(CommunityBO communityBO);

    /**
     * 收藏帖子
     * @param userId
     * @param postId
     */
    boolean favoritePost(String userId, String postId);

    /**
     * 获取帖子收藏状态
     * @param userId
     * @param postId
     * @return
     */
    boolean getPostFavoriteStatus(String userId, String postId);

    /**
     * 点赞帖子
     * @param userId
     * @param postId
     */
    boolean likePost(String userId, String postId, LocalDateTime likeTime);

    /**
     * 获取帖子点赞状态
     * @param userId
     * @param postId
     * @return
     */
    boolean getPostLikeStatus(String userId, String postId);

    /**
     * 置顶帖子
     * @param userId
     * @param postId
     */
    void topPost(String userId, String postId);

    /**
     * 增加帖子收藏数
     * @param postId
     */
    void increaseFavoriteCount(String postId);

    /**
     * 增加帖子点赞数
     * @param postId
     */
    void increaseLikeCount(String postId);

    /**
     * 增加帖子评论数
     * @param postId
     */
    void increaseCommentCount(String postId);

    /**
     * 减少帖子收藏数
     * @param postId
     */
    void decreaseFavoriteCount(String postId);

    /**
     * 减少帖子点赞数
     * @param postId
     */
    void decreaseLikeCount(String postId);

    /**
     * 减少帖子评论数
     * @param postId
     */
    void decreaseCommentCount(String postId);

    /**
     * 统计帖子评论点赞收藏数
     */
    void countAll();

    /**
     * 删除已删除的图片记录
     */
    void deleteMissingImage() throws ClientException;
}
