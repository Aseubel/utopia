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

}
