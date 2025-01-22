package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.bazaar.model.vo.TradeImage;
import com.aseubel.infrastructure.dao.po.Image;
import org.springframework.stereotype.Component;

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

}
