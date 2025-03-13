package com.aseubel.domain.search.model;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @date 2025-03-12 10:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchBO {

    @FieldDesc(name = "搜索内容")
    private String query;

    @FieldDesc(name = "偏移量")
    private Integer offset;

    @FieldDesc(name = "每页数量")
    private Integer limit;

    @FieldDesc(name = "学校代码")
    private String schoolCode;

}
