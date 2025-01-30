package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.bazaar.model.entity.TradeImage;
import com.aseubel.infrastructure.dao.po.Image;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Aseubel
 * @description ImageConvertor
 * @date 2025-01-22 12:07
 */
@Component
public class TradeImageConvertor {

    public Image convert(TradeImage tradeImage) {
        return Image.builder()
                .userId(tradeImage.getUserId())
                .imageId(tradeImage.getImageId())
                .imageUrl(tradeImage.getImageUrl())
                .build();
    }

    public TradeImage convert(Image image) {
        return TradeImage.builder()
                .imageId(image.getImageId())
                .imageUrl(image.getImageUrl())
                .userId(image.getUserId())
                .build();
    }

    public <T, U> List<U> convert(List<T> items, Function<T, U> converter) {
        return items.stream().map(converter).collect(Collectors.toList());
    }

}
