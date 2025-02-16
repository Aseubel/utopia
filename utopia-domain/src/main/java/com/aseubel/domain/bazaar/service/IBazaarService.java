package com.aseubel.domain.bazaar.service;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.bazaar.model.bo.BazaarBO;
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
     * @return
     */
    List<TradePostEntity> listTradePost(BazaarBO bazaarBO);

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
