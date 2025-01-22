package com.aseubel.infrastructure.dao.po;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Aseubel
 * @description 评论帖子图片持久化对象
 * @date 2025-01-21 20:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Image {

    @FieldDesc(name = "id")
    private Long id;

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "图片id")
    private String imageId;
    
    @FieldDesc(name = "图片url")
    private String imageUrl;

    @FieldDesc(name = "创建时间")
    private LocalDateTime createTime;

}
