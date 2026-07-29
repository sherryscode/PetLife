package com.petlife.auth.controller;

import com.petlife.common.response.Result;
import com.petlife.common.utils.FileUploadUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = FileUploadUtil.upload(file);
            Map<String, String> result = new HashMap<>();
            result.put("url", filePath);
            return Result.success("上传成功", result);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (IOException e) {
            return Result.error("上传失败：" + e.getMessage());
        }
    }

    @GetMapping("/**")
    public void serveFile(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getRequestURI();
        
        if (path == null || !path.startsWith("/files/")) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String relativePath = path.substring(7);
        String fullPath = FileUploadUtil.getFilePath(relativePath);
        File file = new File(fullPath);

        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = getContentType(relativePath);
        response.setContentType(contentType);
        response.setContentLength((int) file.length());
        
        response.setHeader("Cache-Control", "public, max-age=86400");
        response.setHeader("Expires", String.valueOf(System.currentTimeMillis() + 86400000L));
        response.setHeader("ETag", String.valueOf(file.lastModified()));

        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String getContentType(String filename) {
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filename.endsWith(".png")) {
            return "image/png";
        } else if (filename.endsWith(".gif")) {
            return "image/gif";
        } else if (filename.endsWith(".webp")) {
            return "image/webp";
        }
        return "application/octet-stream";
    }
}