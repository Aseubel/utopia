package com.aseubel.domain.bazaar.adapter.repo;

import com.aseubel.domain.bazaar.model.bo.BazaarBO;
import com.aseubel.domain.bazaar.model.entity.TradeImage;
import com.aseubel.domain.bazaar.model.entity.TradePostEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aseubel
 * @description 帖子仓储层接口
 * @date 2025-01-30 23:27
 */
@Repository
public interface ITradePostRepository {

    /**
     * 直接列举帖子
     * @return
     */
    List<TradePostEntity> listTradePost(BazaarBO bazaarBO);

    /**
     * 保存帖子图片记录
     * @param postImage
     */
    void savePostImage(TradeImage postImage);

    /**
     * 保存新帖子
     * @param tradePostEntity
     */
    void saveNewTradePost(TradePostEntity tradePostEntity);

    /**
     * 关联帖子图片
     * @param postId
     * @param images
     */
    void relateNewPostImage(String postId, List<TradeImage> images);

    /**
     * 根据帖子id列举帖子
     * @param imageIds
     * @return
     */
    List<TradeImage> listPostImagesByImageIds(List<String> imageIds);

    /**
     * 根据帖子id获取第一张图片
     * @param postId
     * @return
     */
    String getPostFirstImage(String postId);

}
