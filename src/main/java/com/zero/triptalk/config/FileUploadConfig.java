package com.zero.triptalk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class FileUploadConfig {

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        // 최대 파일 크기 및 요청 크기 설정
        resolver.setMaxUploadSize(100 * 1024 * 1024); // 100MB
        resolver.setMaxInMemorySize(10240); // 10KB (메모리에 유지되는 파일 크기)
        return resolver;
    }
}