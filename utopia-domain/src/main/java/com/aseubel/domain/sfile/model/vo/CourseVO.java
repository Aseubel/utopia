package com.aseubel.domain.sfile.model.vo;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Aseubel
 * @date 2025-02-26 22:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseVO {

    @FieldDesc(name = "学科门类名称")
    private String majorName;

    @FieldDesc(name = "课程名称")
    private List<String> courseName;

}
