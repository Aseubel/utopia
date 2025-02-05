package com.aseubel.domain.user.model.vo;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @description 高校信息
 * @date 2025-02-05 13:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class School {

    @FieldDesc(name = "学校代码")
    private String schoolCode;

    @FieldDesc(name = "学校名称")
    private String schoolName;

    @FieldDesc(name = "讨论帖数目")
    private Integer discussPostCount;

    @FieldDesc(name = "交易帖数目")
    private Integer tradePostCount;

    @FieldDesc(name = "学生(用户)人数")
    private Integer studentCount;

}
