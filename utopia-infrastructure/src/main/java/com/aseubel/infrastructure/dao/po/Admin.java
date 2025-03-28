package com.aseubel.infrastructure.dao.po;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @description 管理员
 * @date 2025-03-28 17:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Admin {

    @FieldDesc(name = "id")
    private Integer id;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "管理员类型 0-超级管理员;1-普通管理员")
    private Integer adminType;

    @FieldDesc(name = "所属校区（普通管理员必填，超级管理员可为空）")
    private String schoolCode;

    @FieldDesc(name = "创建时间")
    private LocalDateTime createTime;

    @FieldDesc(name = "更新时间")
    private LocalDateTime updateTime;
    
}
