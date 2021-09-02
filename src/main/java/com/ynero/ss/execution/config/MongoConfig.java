package com.ynero.ss.execution.config;

import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.MongoDBConfiguration;
import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableMongock
@Import(MongoDBConfiguration.class)
public class MongoConfig {
}
