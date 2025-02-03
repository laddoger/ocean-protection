package com.ocean.protection.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    
    private final ImageProperties imageProperties;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String storagePath = imageProperties.getStoragePath();
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + storagePath + "/")
                .setCachePeriod(3600)  // 缓存一小时
                .resourceChain(true);  // 开启资源链
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
} 