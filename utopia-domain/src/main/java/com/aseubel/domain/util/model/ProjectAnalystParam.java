package com.aseubel.domain.util.model;

import cn.hutool.json.JSONUtil;
import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectAnalystParam {

    @FieldDesc(name = "用户id")
    private String userId;

    @FieldDesc(name = "github用户名")
    private String username;

    @FieldDesc(name = "项目仓库名称")
    private String repo;

    @FieldDesc(name = "分支")
    private String ref;

    @FieldDesc(name = "文件url -> file_url")
    private String file_url;

    public String toJson() {
        return JSONUtil.toJsonStr(this);
    }
}
