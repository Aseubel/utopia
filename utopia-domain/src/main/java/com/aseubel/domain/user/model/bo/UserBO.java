package com.aseubel.domain.user.model.bo;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @date 2025-02-18 10:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBO {

    @FieldDesc(name = "用户ID")
    private String userId;

}
