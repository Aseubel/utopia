package com.aseubel.domain.creation.model.aggregate;

import com.aseubel.domain.creation.model.entity.CreationEntity;
import com.aseubel.domain.creation.model.entity.TopicEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @date 2025/4/9 上午1:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OverView {

    private TopicEntity topic;

    private CreationEntity creation;
}
