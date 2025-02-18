package com.aseubel.api.dto.user;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description 注销账户请求
 * @date 2025-02-18 10:22
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancelAccountRequest implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

}
