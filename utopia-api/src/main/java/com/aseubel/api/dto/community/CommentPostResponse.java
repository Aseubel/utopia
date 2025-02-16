package com.aseubel.api.dto.community;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author Aseubel
 * @date 2025-02-16 13:52
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentPostResponse implements Serializable {

    @FieldDesc(name = "评论id")
    private String commentId;

}
