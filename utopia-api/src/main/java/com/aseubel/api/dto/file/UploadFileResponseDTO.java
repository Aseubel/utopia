package com.aseubel.api.dto.file;

import com.aseubel.types.annotation.FieldDesc;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileResponseDTO implements Serializable {

    @FieldDesc(name = "文件url")
    private String fileUrl;

}
