package com.aseubel.api.dto.file;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description 
 * @date 2025-02-26 20:29
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueryCourseRequest implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

//    @FieldDesc(name = "学科门类名称")
//    private String majorName;
//
//    @FieldDesc(name = "课程名称")
//    private String courseName;
    
}
