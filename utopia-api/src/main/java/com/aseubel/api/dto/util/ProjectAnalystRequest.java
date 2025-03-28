package com.aseubel.api.dto.util;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAnalystRequest implements Serializable {

    @FieldDesc(name = "用户id")
    private String userId;
    
    @FieldDesc(name = "github用户名")
    private String username;

    @FieldDesc(name = "项目仓库名称")
    private String repoName;

    @FieldDesc(name = "分支")
    private String branch;

    @FieldDesc(name = "文件url")
    private String fileUrl;
}
