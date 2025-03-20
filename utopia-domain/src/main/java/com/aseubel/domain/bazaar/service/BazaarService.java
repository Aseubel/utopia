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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        // 限制每页显示的帖子数量
        bazaarBO.setLimit(limit == null ? PER_PAGE_TRADE_POST_SIZE : limit);
        // 查询帖子列表
        List<TradePostEntity> tradePostEntities = tradePostRepository.listTradePost(bazaarBO);
        if (CollectionUtil.isEmpty(tradePostEntities)) {
            return Collections.emptyList();
        }
        // 提取帖子的用户id
        List<String> userIds = Optional.of(tradePostEntities)
                .map(d -> d.stream()
                        .map(TradePostEntity::getUserId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        // 获取发帖人的用户名和头像
        Map<String, UserEntity> users = CollectionUtil.isEmpty(userIds) ? Collections.emptyMap() : bazaarUserRepository.queryUserBaseInfo(userIds);
        if (CollectionUtil.isNotEmpty(users)) {
            tradePostEntities.forEach(d -> {
                d.setUserName(users.get(d.getUserId()).getUserName());
                d.setUserAvatar(users.get(d.getUserId()).getAvatar());
            });
        }
        // 获取帖子的第一张图片
        if (!CollectionUtil.isEmpty(tradePostEntities)) {
            tradePostEntities.forEach(d ->
            {
                String imageUrl = tradePostRepository.getPostFirstImage(d.getPostId());
                d.setImage(imageUrl);}
            );
        }
        return tradePostEntities;
    }

    @Override
    public TradeImage uploadPostImage(TradeImage postImage) throws ClientException {
        if (ObjectUtils.isEmpty(bazaarUserRepository.queryUserStatus(postImage.getUserId()))) {
            throw new AppException("用户状态异常，请联系管理员");
        }
        // 上传图片到OSS
        postImage.generateImageId();
        String imageUrl = aliOSSUtil.upload(postImage.getImage(), postImage.getObjectName());
        postImage.setImageUrl(imageUrl);
        // 保存图片信息到数据库
        tradePostRepository.savePostImage(postImage);
        return postImage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String publishTradePost(TradePostEntity tradePostEntity) {
        if (ObjectUtils.isEmpty(bazaarUserRepository.queryUserStatus(tradePostEntity.getUserId()))) {
            throw new AppException("用户状态异常，请联系管理员");
        }

        tradePostEntity.generatePostId();
        tradePostRepository.saveNewTradePost(tradePostEntity);

        if (!CollectionUtil.isEmpty(tradePostEntity.getImages())) {
            List<TradeImage> images = tradePostRepository.listPostImagesByImageIds(tradePostEntity.getImages());
            if (!CollectionUtil.isEmpty(images)) {
                tradePostRepository.relateNewPostImage(tradePostEntity.getPostId(), images);
            }
        }

        return tradePostEntity.getPostId();
    }

    @Override
    public List<TradePostEntity> queryMyTradePosts(BazaarBO bazaarBO) {
        String postId = bazaarBO.getPostId();
        Integer limit = bazaarBO.getLimit();
        // 限制每页显示的帖子数量
        bazaarBO.setLimit(limit == null ? PER_PAGE_TRADE_POST_SIZE : limit);
        // 查询帖子列表
        List<TradePostEntity> tradePostEntities = tradePostRepository.listUserTradePost(bazaarBO);
        if (CollectionUtil.isEmpty(tradePostEntities)) {
            return Collections.emptyList();
        }
        // 提取帖子的用户id
        List<String> userIds = Optional.of(tradePostEntities)
                .map(d -> d.stream()
                        .map(TradePostEntity::getUserId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        // 获取发帖人的用户名和头像
        Map<String, UserEntity> users = CollectionUtil.isEmpty(userIds) ? Collections.emptyMap() : bazaarUserRepository.queryUserBaseInfo(userIds);
        if (CollectionUtil.isNotEmpty(users)) {
            tradePostEntities.forEach(d -> {
                d.setUserName(users.get(d.getUserId()).getUserName());
                d.setUserAvatar(users.get(d.getUserId()).getAvatar());
            });
        }
        // 获取帖子的第一张图片
        if (!CollectionUtil.isEmpty(tradePostEntities)) {
            tradePostEntities.forEach(d ->
                    {
                        String imageUrl = tradePostRepository.getPostFirstImage(d.getPostId());
                        d.setImage(imageUrl);}
            );
        }
        return tradePostEntities;
    }

    @Override
    public TradePostEntity queryPostDetail(String postId) {
        TradePostEntity tradePost = null;
        try {
            tradePost = tradePostRepository.queryPostDetail(postId);
        } catch (NullPointerException e) {
            throw new AppException("帖子不存在！");
        }
        tradePost.setImages(tradePostRepository.listPostImages(postId));
        return tradePost;
    }

    @Override
    public void deletePost(BazaarBO bazaarBO) {
        verifyPostAuth(bazaarBO.getUserId(), bazaarBO.getPostId());
        tradePostRepository.deletePost(bazaarBO.getPostId());
    }

    @Override
    public void completeTrade(BazaarBO bazaarBO) {
        verifyPostAuth(bazaarBO.getUserId(), bazaarBO.getPostId());
        tradePostRepository.completeTrade(bazaarBO.getPostId());
    }

    private void verifyPostAuth(String userId, String postId) {
        if (!userId.equals(tradePostRepository.getUserIdByPostId(postId))) {
            log.error("用户无权限操作帖子！, user={}, post={}", userId, postId);
            throw new AppException("用户无权操作帖子！");
        }
    }

}
