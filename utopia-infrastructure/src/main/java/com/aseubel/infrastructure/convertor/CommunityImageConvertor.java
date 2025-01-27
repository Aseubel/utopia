package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.community.model.entity.CommunityImage;
import com.aseubel.infrastructure.dao.po.Image;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Aseubel
 * @description ImageConvertor
 * @date 2025-01-21 20:39
 */
@Component
public class CommunityImageConvertor {

    public Image convert(CommunityImage communityImage) {
        return Image.builder()
                .userId(communityImage.getUserId())
                .imageId(communityImage.getImageId())
                .imageUrl(communityImage.getImageUrl())
                .build();
    }

    public CommunityImage convert(Image image) {
        return CommunityImage.builder()
                .imageId(image.getImageId())
                .imageUrl(image.getImageUrl())
                .userId(image.getUserId())
                .build();
    }

    public <T, U> List<U> convert(List<T> items, Function<T, U> converter) {
        return items.stream().map(converter).collect(Collectors.toList());
    }

}
