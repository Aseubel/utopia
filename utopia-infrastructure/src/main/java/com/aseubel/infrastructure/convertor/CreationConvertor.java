package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.creation.model.entity.CreationEntity;
import com.aseubel.infrastructure.dao.po.Creation;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Aseubel
 * @date 2025/4/8 上午11:45
 */
public class CreationConvertor {

    public static Creation convert(CreationEntity creation) {
        return Creation.builder()
                .creationId(creation.getCreationId())
                .topicId(creation.getTopicId())
                .rank(creation.getRank())
                .content(creation.getContent())
                .voteCount(creation.getVoteCount())
                .build();
    }

    public static CreationEntity convert(Creation creation) {
        return CreationEntity.builder()
                .creationId(creation.getCreationId())
                .topicId(creation.getTopicId())
                .rank(creation.getRank())
                .content(creation.getContent())
                .voteCount(creation.getVoteCount())
                .build();
    }

    public static <T, U> List<U> convert(List<T> items, Function<T, U> converter) {
        return items.stream().map(converter).collect(Collectors.toList());
    }
}
