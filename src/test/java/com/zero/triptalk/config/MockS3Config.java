package com.zero.triptalk.config;

import com.amazonaws.services.s3.AmazonS3Client;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockS3Config extends S3Config{

    @Bean
    @Override
    public AmazonS3Client amazonS3Client() {
        return Mockito.mock(AmazonS3Client.class);
    }
}
