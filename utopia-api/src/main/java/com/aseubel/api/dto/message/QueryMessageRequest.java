package com.aseubel.api.dto.message;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Aseubel
 * @date 2025-03-19 22:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryMessageRequest implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "另一用户id")
    private String otherId;

    @FieldDesc(name = "上次查询最后一条message的id")
    private String messageId;

    @FieldDesc(name = "查询数量")
    private Integer limit;

}
