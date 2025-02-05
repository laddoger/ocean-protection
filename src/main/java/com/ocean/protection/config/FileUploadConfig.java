package com.ocean.protection.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;

@Slf4j
@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保上传目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            log.info("创建上传目录: {}", uploadPath);
            if (!uploadDir.mkdirs()) {
                log.error("无法创建上传目录: {}", uploadPath);
            }
        }

        // 设置目录权限
        uploadDir.setReadable(true, false);
        uploadDir.setWritable(true, false);
        uploadDir.setExecutable(true, false);

        String uploadLocation = "file:" + uploadPath + File.separator;
        log.info("配置资源映射: /files/** -> {}", uploadLocation);
        
        registry.addResourceHandler("/files/**")
                .addResourceLocations(uploadLocation)
                .setCachePeriod(3600)
                .resourceChain(true);
    }
} 