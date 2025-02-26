package com.aseubel.api.dto.file;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Aseubel
 * @date 2025-02-26 22:09
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryCourseResponse implements Serializable {

    @FieldDesc(name = "学科门类名称")
    private String majorName;

    @FieldDesc(name = "课程名称")
    private List<String> courseNames;

}
