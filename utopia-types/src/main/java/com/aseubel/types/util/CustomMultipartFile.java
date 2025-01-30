package com.aseubel.types.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

public class CustomMultipartFile implements MultipartFile {
    private final byte[] fileContent;
    private final String fileName;

    public CustomMultipartFile(byte[] fileContent, String fileName) {
        this.fileContent = fileContent;
        this.fileName = fileName;
    }

    @NotNull
    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public String getOriginalFilename() {
        return fileName;
    }

    @Override
    public String getContentType() {
        return "image/jpeg"; // 根据需要设置正确的MIME类型
    }

    @Override
    public boolean isEmpty() {
        return fileContent == null || fileContent.length == 0;
    }

    @Override
    public long getSize() {
        return fileContent.length;
    }

    @NotNull
    @Override
    public byte[] getBytes() throws IOException {
        return fileContent;
    }

    @NotNull
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (OutputStream out = Files.newOutputStream(dest.toPath())) {
            out.write(fileContent);
        }
    }
}
