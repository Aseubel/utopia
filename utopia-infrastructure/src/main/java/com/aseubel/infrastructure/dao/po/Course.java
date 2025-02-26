package com.aseubel.infrastructure.dao.po;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @description 学科门类下的课程
 * @date 2025-02-26 18:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {

    @FieldDesc(name = "id")
    private Integer id;

    @FieldDesc(name = "courseName")
    private String courseName;

    @FieldDesc(name = "majorName")
    private String majorName;
    
}
