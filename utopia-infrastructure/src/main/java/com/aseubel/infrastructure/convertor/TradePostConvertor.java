package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.bazaar.model.entity.TradePostEntity;
import com.aseubel.infrastructure.dao.po.TradePost;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TradePostConvertor {

    public TradePostEntity convert(TradePost tradePost) {
        return TradePostEntity.builder()
                .id(tradePost.getId())
                .postId(tradePost.getTradePostId())
                .userId(tradePost.getUserId())
                .title(tradePost.getTitle())
                .content(tradePost.getContent())
                .price(tradePost.getPrice())
                .contact(tradePost.getContact())
                .schoolCode(tradePost.getSchoolCode())
                .type(tradePost.getType())
                .status(tradePost.getStatus())
                .createTime(tradePost.getCreateTime())
                .updateTime(tradePost.getUpdateTime())
                .build();
    }

    public TradePost convert(TradePostEntity tradePostEntity) {
        return TradePost.builder()
                .tradePostId(tradePostEntity.getPostId())
                .userId(tradePostEntity.getUserId())
                .title(tradePostEntity.getTitle())
                .content(tradePostEntity.getContent())
                .price(tradePostEntity.getPrice())
                .contact(tradePostEntity.getContact())
                .schoolCode(tradePostEntity.getSchoolCode())
                .type(tradePostEntity.getType())
                .status(tradePostEntity.getStatus())
                .createTime(tradePostEntity.getCreateTime())
                .updateTime(tradePostEntity.getUpdateTime())
                .build();
    }

    public <T, U> List<U> convert(List<T> items, Function<T, U> converter) {
        return items.stream().map(converter).collect(Collectors.toList());
    }
    
}
