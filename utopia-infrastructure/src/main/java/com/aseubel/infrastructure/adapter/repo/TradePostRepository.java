package com.aseubel.infrastructure.adapter.repo;

import com.aseubel.domain.bazaar.adapter.repo.ITradePostRepository;
import com.aseubel.domain.bazaar.model.bo.BazaarBO;
import com.aseubel.domain.bazaar.model.entity.TradeImage;
import com.aseubel.domain.bazaar.model.entity.TradePostEntity;
import com.aseubel.domain.user.model.entity.UserEntity;
import com.aseubel.infrastructure.convertor.TradeImageConvertor;
import com.aseubel.infrastructure.convertor.TradePostConvertor;
import com.aseubel.infrastructure.dao.ImageMapper;
import com.aseubel.infrastructure.dao.TradePostMapper;
import com.aseubel.infrastructure.dao.po.TradePost;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Aseubel
 * @description 交易帖子仓储层实现类
 * @date 2025-01-30 23:05
 */
@Repository
public class TradePostRepository implements ITradePostRepository {

    @Resource
    private TradePostMapper tradePostMapper;

    @Resource
    private TradePostConvertor tradePostConvertor;

    @Resource
    private ImageMapper imageMapper;

    @Resource
    private TradeImageConvertor tradeImageConvertor;

    @Override
    public List<TradePostEntity> listTradePost(BazaarBO bazaarBO) {
        String postId = bazaarBO.getPostId();
        Integer limit = bazaarBO.getLimit();

        return Optional.ofNullable(StringUtils.isEmpty(postId)
                        ? tradePostMapper.listTradePostAhead(limit, bazaarBO.getType(), bazaarBO.getStatus())
                        : tradePostMapper.listTradePost(postId, limit, bazaarBO.getType(), bazaarBO.getStatus()))
                .map(p -> p.stream().map(tradePostConvertor::convert).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public void savePostImage(TradeImage postImage) {
        imageMapper.addImage(tradeImageConvertor.convert(postImage));
    }

    @Override
    public void saveNewTradePost(TradePostEntity tradePostEntity) {
        TradePost tradePost = tradePostConvertor.convert(tradePostEntity);
        tradePostMapper.addTradePost(tradePost);
    }

    @Override
    public void relateNewPostImage(String postId, List<TradeImage> images) {
        tradePostMapper.relateTradePostImage(postId, tradeImageConvertor.convert(images, tradeImageConvertor::convert));
    }

    @Override
    public List<TradeImage> listPostImagesByImageIds(List<String> imageIds) {
        return tradeImageConvertor.convert(
                imageMapper.listImageByImageIds(imageIds), tradeImageConvertor::convert);
    }

    @Override
    public String getPostFirstImage(String postId) {
        // 先获取第一张图片的id。再去image表中查询图片的url
        return Optional.ofNullable(tradePostMapper.getPostFirstImage(postId)).map(imageMapper::getImageUrl).orElse(null);
    }

}
