package com.aseubel.domain.search.model;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @FieldDesc(name = "is_deleted")
    private int isDeleted;

    public static FileVO parseFrom(List<CanalEntry.Column> afterColumns) {
        FileVO vo = new FileVO();
        Map<String, String> columnMap = afterColumns.stream()
                .collect(Collectors.toMap(CanalEntry.Column::getName, CanalEntry.Column::getValue));

        Optional.ofNullable(columnMap.get("id")).map(Long::parseLong).ifPresent(vo::setId);
        Optional.ofNullable(columnMap.get("sfile_id")).ifPresent(vo::setFileId);
        Optional.ofNullable(columnMap.get("sfile_name")).ifPresent(vo::setFileName);
        Optional.ofNullable(columnMap.get("sfile_url")).ifPresent(vo::setFileUrl);
        Optional.ofNullable(columnMap.get("course_name")).ifPresent(vo::setCourseName);
        Optional.ofNullable(columnMap.get("sfile_size")).map(Long::parseLong).ifPresent(vo::setFileSize);
        Optional.ofNullable(columnMap.get("download_count")).map(Long::parseLong).ifPresent(vo::setDownloadCount);
        Optional.ofNullable(columnMap.get("is_deleted")).map(Integer::parseInt).ifPresent(vo::setIsDeleted);

        return vo;
    }

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
