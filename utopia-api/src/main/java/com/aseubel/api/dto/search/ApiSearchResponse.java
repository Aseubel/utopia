package com.aseubel.api.dto.search;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * @author Aseubel
 * @description Search response object
 * @date 2025-03-11 22:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiSearchResponse implements Serializable {

    @FieldDesc(name = "搜索结果")
    private List<HashMap<String, Object>> searchResult;

    @FieldDesc(name = "搜索结果总数")
    private int totalCount;

    @FieldDesc(name = "搜索耗时/ms")
    private int searchTime;

}
