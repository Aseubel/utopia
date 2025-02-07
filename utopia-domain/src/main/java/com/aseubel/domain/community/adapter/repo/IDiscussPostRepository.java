package com.aseubel.domain.community.adapter.repo;

import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;
import org.springframework.stereotype.Repository;

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
     * @param postId 上一页查询最后一个帖子的id
     * @param limit 每页显示数量
     * @return
     */
    List<DiscussPostEntity> listDiscussPost(String postId, Integer limit);

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
     * @param userId 用户id
     * @param postId 相当于游标，上一页最后一个帖子的id
     * @param limit 每页显示数量
     * @return
     */
    List<DiscussPostEntity> queryUserFavoritePosts(String userId, String postId, Integer limit);
}
