package com.aseubel.api.dto.bazaar;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025-03-02 22:32
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryPostDetailRequest implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "帖子id")
    private String postId;

}
