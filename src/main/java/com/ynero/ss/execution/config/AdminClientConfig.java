package com.ynero.ss.execution.config;

import lombok.Setter;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.List;

@Configuration
public class AdminClientConfig {

    @Setter(onMethod_ = {@Value("${spring.producer.bootstrap-servers}")})
    private String bootstrapServers;

    @Setter(onMethod_ = {@Autowired})
    private KafkaAdmin kafkaAdmin;

    @Bean
    public AdminClient getAdminClient(){
        var properties = new HashMap<String, Object>();
        properties.putAll(kafkaAdmin.getConfigurationProperties());
        properties.put("bootstrap.servers", List.of(bootstrapServers));
        return AdminClient.create(properties);
    }
}
