package com.aseubel.api.dto.user;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryUserInfoRequestDTO implements Serializable {

    @NotNull(message = "用户openid不能为空")
    @FieldDesc(name = "用户openid")
    private String userId;

}
