package com.aseubel.api.dto.bazaar;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025-03-02 23:02
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublishPostResponse implements Serializable {

    @FieldDesc(name = "postId")
    private String postId;

}
