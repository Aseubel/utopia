package com.aseubel.domain.search.model;

import cn.hutool.core.util.StrUtil;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.aseubel.types.annotation.FieldDesc;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    /**
     * 从binlog获取数据
     */
    public static FileVO parseFrom(Serializable[] afterRowData) {
        List<Serializable> list = Arrays.stream(afterRowData).toList();
        FileVO vo = new FileVO();
        vo.setId((Long) list.get(0));
        vo.setFileId((String) list.get(1));
        vo.setFileName((String) list.get(2));
        vo.setFileUrl((String) list.get(3));
        vo.setFileSize((Long) list.get(4));
        vo.setDownloadCount((Integer) list.get(5));
        vo.setCourseName((String) list.get(6));
        vo.setIsDeleted((Integer) list.get(11));
        return vo;
    }

    /**
     * 从canal的afterColumns中解析出TradePostVO对象
     */
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
