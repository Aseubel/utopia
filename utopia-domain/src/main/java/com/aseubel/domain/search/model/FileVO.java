package com.aseubel.domain.search.model;

import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aseubel
 * @date 2025-03-14 19:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileVO {

    @FieldDesc(name = "id")
    private long id;

    @FieldDesc(name = "文件id")
    private String fileId;

    @FieldDesc(name = "文件名称")
    private String fileName;

    @FieldDesc(name = "文件路径")
    private String fileUrl;

    @FieldDesc(name = "课程名称")
    private String courseName;

    @FieldDesc(name = "文件大小")
    private long fileSize;

    @FieldDesc(name = "下载次数")
    private long downloadCount;

    /**
     * 转换成json字符串
     */
    public String toJsonString() {
        return "[{" +
                "\"fileId\":\"" + fileId + '\"' +
                ", \"fileName\":\"" + fileName + '\"' +
                ", \"fileUrl\":\"" + fileUrl + '\"' +
                ", \"fileSize\":\"" + fileSize + '\"' +
                ", \"courseName\":\"" + courseName + '\"' +
                ", \"downloadCount\":\"" + downloadCount + '\"' +
                "}]";
    }

}
