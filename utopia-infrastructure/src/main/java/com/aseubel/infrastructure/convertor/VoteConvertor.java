package com.aseubel.infrastructure.convertor;

import com.aseubel.domain.creation.model.entity.VoteEntity;
import com.aseubel.infrastructure.dao.po.Vote;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Aseubel
 * @date 2025/4/8 上午11:39
 */
public class VoteConvertor {

    public static Vote convert(VoteEntity vote) {
        return Vote.builder()
                .voteId(vote.getVoteId())
                .userId(vote.getUserId())
                .topicId(vote.getTopicId())
                .creationId(vote.getCreationId())
                .type(vote.getType())
                .build();
    }

    public static VoteEntity convert(Vote vote) {
        return VoteEntity.builder()
                .voteId(vote.getVoteId())
                .userId(vote.getUserId())
                .topicId(vote.getTopicId())
                .creationId(vote.getCreationId())
                .type(vote.getType())
                .build();
    }

    public static <T, U> List<U> convert(List<T> items, Function<T, U> converter) {
        return items.stream().map(converter).collect(Collectors.toList());
    }
}
