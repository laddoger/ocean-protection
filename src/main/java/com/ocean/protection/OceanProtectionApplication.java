package com.ocean.protection;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ocean.protection.mapper")
public class OceanProtectionApplication {
    public static void main(String[] args) {
        SpringApplication.run(OceanProtectionApplication.class, args);
    }
} 