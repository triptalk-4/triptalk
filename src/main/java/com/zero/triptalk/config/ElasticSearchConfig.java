package com.zero.triptalk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import java.net.InetSocketAddress;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.hostname}")
    private String hostname;
    @Value("${spring.elasticsearch.port}")
    private Integer port;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.create(InetSocketAddress.createUnresolved(hostname, port));
    }
}
