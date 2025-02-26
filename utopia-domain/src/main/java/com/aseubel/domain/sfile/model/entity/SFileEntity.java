package com.aseubel.domain.sfile.model.entity;

import com.aseubel.types.annotation.FieldDesc;
import com.aseubel.types.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.aseubel.types.common.Constant.APP;

/**
 * @author Aseubel
 * @description 领域层分享文件实体类
 * @date 2025-01-20 14:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SFileEntity {

    @FieldDesc(name = "文件本体")
    private MultipartFile sfile;

    @FieldDesc(name = "上传者id")
    private String uploaderId;

    @FieldDesc(name = "文件业务id")
    private String sfileId;

    @FieldDesc(name = "文件名称")
    private String sfileName;

    @FieldDesc(name = "文件路径")
    private String sfileUrl;

    @FieldDesc(name = "文件大小")
    private Long sfileSize;

    @FieldDesc(name = "课程名称")
    private String courseName;

    @FieldDesc(name = "下载次数")
    private Integer downloadCount;

    @FieldDesc(name = "创建时间")
    private LocalDateTime createTime;

    /**
     * 获取在OSS中的文件名称（在类型文件夹下）
     */
    public String generateObjectName() {
        if (courseName == null || sfileName == null) {
            throw new AppException("课程名称或文件名不能为空");
        }
        StringBuilder objectName = new StringBuilder();
        objectName.append(APP).append("/").append(courseName).append("/").append(sfileName);
        return objectName.toString();
    }

//    public SFileEntity(MultipartFile file) {
//        this.sfile = file;
//        this.sfileId = UUID.randomUUID().toString();
//        this.sfileName = Optional.ofNullable(file.getOriginalFilename()).orElse("未命名文件"+sfileId.substring(0, 8));
//        this.sfileSize = file.getSize();
//    }

    public SFileEntity(MultipartFile file, String fileName, String userId, String fileType) {
        this.sfile = file;
        this.uploaderId = userId;
        this.courseName = fileType;
        this.sfileId = UUID.randomUUID().toString();
        this.sfileName = StringUtils.isEmpty(fileName) ? file.getOriginalFilename() : (fileName+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
        this.sfileSize = file.getSize();
    }

    public SFileEntity(MultipartFile file, String userId, String fileType) {
        this.sfile = file;
        this.uploaderId = userId;
        this.courseName = fileType;
        this.sfileId = UUID.randomUUID().toString();
        this.sfileSize = file.getSize();
    }

}
