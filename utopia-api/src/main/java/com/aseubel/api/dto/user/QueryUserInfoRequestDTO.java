package com.aseubel.api.dto.user;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryUserInfoRequestDTO implements Serializable {

    @FieldDesc(name = "用户openid")
    private String userId;

}
