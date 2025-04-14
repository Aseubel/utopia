package com.aseubel.domain.bazaar.service;

import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.bazaar.model.bo.BazaarBO;
import com.aseubel.domain.bazaar.model.entity.TradeImage;
import com.aseubel.domain.bazaar.model.entity.TradePostEntity;
import com.aseubel.domain.community.model.entity.DiscussPostEntity;

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
    String publishTradePost(TradePostEntity tradePostEntity);

    /**
     * 查询我帖子
     * @param bazaarBO
     * @return
     */
    List<TradePostEntity> queryMyTradePosts(BazaarBO bazaarBO);

    /**
     * 查询帖子详情
     * @param postId
     * @return
     */
    TradePostEntity queryPostDetail(String postId);

    /**
     * 删除帖子
     * @param bazaarBO
     */
    void deletePost(BazaarBO bazaarBO);

    /**
     * 完成帖子交易
     * @param bazaarBO
     */
    void completeTrade(BazaarBO bazaarBO);

    /**
     * 审核帖子
     * @param post 帖子实体
     * @return 审核结果 true-通过;false-不通过
     */
    boolean auditPost(TradePostEntity post);
}
