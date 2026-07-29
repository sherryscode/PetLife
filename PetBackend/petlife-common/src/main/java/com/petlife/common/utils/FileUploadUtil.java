package com.petlife.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class FileUploadUtil {

    private static final String BASE_PATH = "c:/Study/实训/PetLife/PetBackend/picture";
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};

    public static String upload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!isAllowedExtension(extension)) {
            throw new IllegalArgumentException("不支持的文件类型，仅支持: " + String.join(", ", ALLOWED_EXTENSIONS));
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String newFilename = UUID.randomUUID().toString().replace("-", "") + extension;
        String relativePath = timestamp + "/" + newFilename;
        String fullPath = BASE_PATH + "/" + relativePath;

        File destFile = new File(fullPath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        file.transferTo(destFile);

        return "/api/files/" + relativePath;
    }

    private static boolean isAllowedExtension(String extension) {
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    public static String getFilePath(String relativePath) {
        return BASE_PATH + "/" + relativePath.replace("/api/files/", "");
    }
}