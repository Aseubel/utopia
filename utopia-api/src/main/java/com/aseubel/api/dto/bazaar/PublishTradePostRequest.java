package com.aseubel.api.dto.bazaar;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author Aseubel
 * @description 发布交易帖子请求参数
 * @date 2025-01-30 23:50
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishTradePostRequest implements Serializable {

    @NotNull(message = "用户ID不能为空")
    @FieldDesc(name = "用户ID")
    private String userId;

    @NotNull(message = "标题不能为空")
    @FieldDesc(name = "标题")
    private String title;

    @NotNull(message = "内容不能为空")
    @FieldDesc(name = "内容")
    private String content;

    @NotNull(message = "价格不能为空")
    @FieldDesc(name = "价格")
    private Double price;

    @NotNull(message = "交易类型不能为空")
    @FieldDesc(name = "交易类型, 0-出售;1-求购:2-赠送")
    private Integer type;

    @NotNull(message = "联系方式不能为空")
    @NotBlank(message = "联系方式不能为空")
    @FieldDesc(name = "联系方式")
    private String contact;

    @NotNull(message = "学校代码不能为空")
    @NotBlank(message = "学校代码不能为空")
    @FieldDesc(name = "学校代码")
    private String schoolCode;

    @FieldDesc(name = "标签")
    private List<String> tags;

    @FieldDesc(name = "图片")
    private List<String> images;

}
