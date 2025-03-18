package com.aseubel.api.dto.community;

import com.aseubel.types.annotation.FieldDesc;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

/**
 * @author Aseubel
 * @description 社区回复通知查询请求
 * @date 2025-03-18 19:51
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryNoticeRequest implements Serializable {

    @NotNull(message = "用户userId不能为空")
    @FieldDesc(name = "用户userId")
    private String userId;

    @FieldDesc(name = "上一页最后的通知id")
    private String noticeId;

    @FieldDesc(name = "每页显示数量")
    private Integer limit;

}
