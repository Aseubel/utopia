package com.aseubel.api.dto.community;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author Aseubel
 * @description 发布帖子请求参数
 * @date 2025-01-27 18:23
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishDiscussPostRequest implements Serializable {

    @NotNull(message = "用户ID不能为空")
    @FieldDesc(name = "用户ID")
    private String userId;

    @NotNull(message = "所属院校代码不能为空")
    @FieldDesc(name = "所属院校代码")
    private String schoolCode;

    @NotNull(message = "标题不能为空")
    @FieldDesc(name = "标题")
    private String title;

    @NotNull(message = "内容不能为空")
    @FieldDesc(name = "内容")
    private String content;

    @FieldDesc(name = "标签")
    private String tag;

    @FieldDesc(name = "图片")
    private List<String> images;

}
