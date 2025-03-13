package com.aseubel.api.dto.search;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description SearchRequest
 * @date 2025-03-11 22:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest implements Serializable {

    @FieldDesc(name = "搜索内容")
    private String query;

    @FieldDesc(name = "偏移量")
    private Integer offset;

    @FieldDesc(name = "每页数量")
    private Integer limit;

    @FieldDesc(name = "学校代码")
    private String schoolCode;
    
}
