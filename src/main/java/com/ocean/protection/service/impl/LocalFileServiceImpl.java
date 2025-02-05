package com.ocean.protection.service.impl;

import com.ocean.protection.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
public class LocalFileServiceImpl implements FileService {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.access.url}")
    private String accessUrl;

    @Override
    public String uploadFile(MultipartFile file, String directory) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("文件为空");
        }

        // 规范化目录路径
        String fullPath = uploadPath + File.separator + directory;
        Path uploadDir = Paths.get(fullPath);

        // 确保目录存在并设置权限
        if (!Files.exists(uploadDir)) {
            log.info("创建上传目录: {}", fullPath);
            try {
                Files.createDirectories(uploadDir);
                File dirFile = uploadDir.toFile();
                dirFile.setReadable(true, false);
                dirFile.setWritable(true, false);
                dirFile.setExecutable(true, false);
            } catch (IOException e) {
                log.error("创建目录失败: {}", fullPath, e);
                throw new IOException("无法创建上传目录: " + fullPath);
            }
        }

        // 生成文件名
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;
        Path filePath = uploadDir.resolve(filename);

        log.info("开始保存文件: {}", filePath);

        // 保存文件
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            log.info("文件保存成功: {}", filePath);

            // 设置文件权限
            File savedFile = filePath.toFile();
            savedFile.setReadable(true, false);
            savedFile.setWritable(true, false);
            
            // 验证文件是否存在且可读
            if (!savedFile.exists() || !savedFile.canRead()) {
                throw new IOException("文件保存失败或无法访问");
            }

            // 记录文件信息
            log.info("文件大小: {} bytes", savedFile.length());
            log.info("文件权限: readable={}, writable={}", savedFile.canRead(), savedFile.canWrite());

            String fileUrl = accessUrl + "/" + directory + "/" + filename;
            log.info("文件访问URL: {}", fileUrl);
            return fileUrl;
        } catch (IOException e) {
            log.error("保存文件失败: {}", filePath, e);
            throw new IOException("文件保存失败: " + e.getMessage());
        }
    }
} 