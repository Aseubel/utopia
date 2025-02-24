package com.aseubel.api.dto.user;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description 查询他人信息
 * @date 2025-02-24 12:05
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueryOtherInfoRequest implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "目标用户id")
    private String targetId;

}
