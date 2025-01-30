package com.aseubel.domain.bazaar.service;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.bazaar.model.entity.TradeImage;
import com.aseubel.domain.bazaar.model.entity.TradePostEntity;

import java.util.List;

/**
 * @author Aseubel
 * @description 集市服务接口
 * @date 2025-01-30 23:36
 */
public interface IBazaarService {

    /**
     * 获取帖子列表
     * @param postId 上一页查询最后一个帖子的id
     * @param limit 每页显示数量
     * @return
     */
    List<TradePostEntity> listTradePost(String postId, Integer limit);

    /**
     * 上传帖子图片
     * @param postImage
     * @return
     */
    TradeImage uploadPostImage(TradeImage postImage) throws ClientException;

    /**
     * 发布帖子
     * @param tradePostEntity
     */
    void publishTradePost(TradePostEntity tradePostEntity);
    
}
