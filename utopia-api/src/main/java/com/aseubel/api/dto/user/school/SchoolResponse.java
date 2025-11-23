package com.aseubel.api.dto.user.school;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025/11/23 下午1:36
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchoolResponse implements Serializable {
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
