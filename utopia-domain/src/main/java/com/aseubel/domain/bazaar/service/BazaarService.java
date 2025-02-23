package com.aseubel.domain.bazaar.service;

import cn.hutool.core.collection.CollectionUtil;
import com.aliyuncs.exceptions.ClientException;
import com.aseubel.domain.bazaar.adapter.repo.IBazaarUserRepository;
import com.aseubel.domain.bazaar.adapter.repo.ITradePostRepository;
import com.aseubel.domain.bazaar.model.bo.BazaarBO;
import com.aseubel.domain.bazaar.model.entity.TradeImage;
import com.aseubel.domain.bazaar.model.entity.TradePostEntity;
import com.aseubel.domain.user.model.entity.UserEntity;
import com.aseubel.types.exception.AppException;
import com.aseubel.types.util.AliOSSUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.aseubel.types.common.Constant.PER_PAGE_DISCUSS_POST_SIZE;
import static com.aseubel.types.common.Constant.PER_PAGE_TRADE_POST_SIZE;

/**
 * @author Aseubel
 * @description 集市服务实现类
 * @date 2025-01-30 23:43
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BazaarService implements IBazaarService{

    private final ITradePostRepository tradePostRepository;

    private final IBazaarUserRepository bazaarUserRepository;

    private final AliOSSUtil aliOSSUtil;

    @Override
    public List<TradePostEntity> listTradePost(BazaarBO bazaarBO) {
        String postId = bazaarBO.getPostId();
        Integer limit = bazaarBO.getLimit();
        log.info("获取集市帖子列表服务开始执行");
        // 限制每页显示的帖子数量
        bazaarBO.setLimit(limit == null ? PER_PAGE_TRADE_POST_SIZE : limit);
        // 查询帖子列表
        List<TradePostEntity> tradePostEntities = tradePostRepository.listTradePost(bazaarBO);
        // 提取帖子的用户id
        List<String> userIds = Optional.ofNullable(tradePostEntities)
                .map(d -> d.stream()
                        .map(TradePostEntity::getUserId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        // 获取发帖人的用户名和头像，repo层已经保证了顺序
        List<UserEntity> users = CollectionUtil.isEmpty(userIds) ? Collections.emptyList() : bazaarUserRepository.queryUserBaseInfo(userIds);
        if (!CollectionUtil.isEmpty(tradePostEntities) && !CollectionUtil.isEmpty(users)) {
            for (int i = 0;i < tradePostEntities.size();i++) {
                tradePostEntities.get(i).setUserName(users.get(i).getUserName());
                tradePostEntities.get(i).setUserAvatar(users.get(i).getAvatar());
            }
        }
        // 获取帖子的第一张图片
        if (!CollectionUtil.isEmpty(tradePostEntities)) {
            tradePostEntities.forEach(d ->
            {
                String imageUrl = tradePostRepository.getPostFirstImage(d.getTradePostId());
                d.setImage(imageUrl);}
            );
        }
        log.info("获取集市帖子列表服务结束执行");
        return tradePostEntities;
    }

    @Override
    public TradeImage uploadPostImage(TradeImage postImage) throws ClientException {
        log.info("上传集市帖子图片服务开始执行，userId:{}", postImage.getUserId());
        if (ObjectUtils.isEmpty(bazaarUserRepository.queryUserStatus(postImage.getUserId()))) {
            throw new AppException("用户状态异常，请联系管理员");
        }
        // 上传图片到OSS
        postImage.generateImageId();
        String imageUrl = aliOSSUtil.upload(postImage.getImage(), postImage.getObjectName());
        postImage.setImageUrl(imageUrl);
        // 保存图片信息到数据库
        tradePostRepository.savePostImage(postImage);
        log.info("上传集市帖子图片服务结束执行，userId:{}", postImage.getUserId());
        return postImage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishTradePost(TradePostEntity tradePostEntity) {
        log.info("发布集市帖子服务开始执行，userId:{}", tradePostEntity.getUserId());
        if (ObjectUtils.isEmpty(bazaarUserRepository.queryUserStatus(tradePostEntity.getUserId()))) {
            throw new AppException("用户状态异常，请联系管理员");
        }

        tradePostEntity.generatePostId();
        tradePostRepository.saveNewTradePost(tradePostEntity);

        if (!CollectionUtil.isEmpty(tradePostEntity.getImages())) {
            List<TradeImage> images = tradePostRepository.listPostImagesByImageIds(tradePostEntity.getImages());
            if (!CollectionUtil.isEmpty(images)) {
                tradePostRepository.relateNewPostImage(tradePostEntity.getTradePostId(), images);
            }
        }

        log.info("发布集市帖子服务结束执行，userId:{}", tradePostEntity.getUserId());
    }
    
}
