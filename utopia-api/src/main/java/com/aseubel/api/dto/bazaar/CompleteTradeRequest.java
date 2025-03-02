package com.aseubel.api.dto.bazaar;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

/**
 * @author Aseubel
 * @date 2025-03-02 23:51
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompleteTradeRequest {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

}
