package com.aseubel.infrastructure.dao.po;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @description 头像持久化对象
 * @date 2025-01-21 13:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Avatar {

    @FieldDesc(name = "id")
    private Long id;

    @FieldDesc(name = "user_id")
    private String userId;

    @FieldDesc(name = "avatar_url")
    private String avatarUrl;

    @FieldDesc(name = "create_time")
    private LocalDateTime createTime;
    
}
