package com.ynero.ss.execution;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureDataMongo
@ActiveProfiles("integration-test")
@Testcontainers
@Slf4j
@DirtiesContext
public class IntegrationTestSetUp {
    public static final String MONGO_VERSION = "4.4.4";
    public static final String REDIS_VERSION = "5.0.7";
    private static final long REDIS_MEMORY = 1024*1024*1024;
    @Autowired
    protected ReactiveMongoOperations mongo;
    @Container
    protected static final MongoDBContainer MONGO_CONTAINER = new MongoDBContainer("mongo:" + MONGO_VERSION);

    @Container
    protected static final GenericContainer REDIS = new GenericContainer<>(DockerImageName.parse("redis:" + REDIS_VERSION))
            //.withClasspathResourceMapping("redis.conf", "./redis.conf", BindMode.READ_ONLY)
            .withCreateContainerCmdModifier(cmd -> cmd.getHostConfig()
                    .withMemory(REDIS_MEMORY)
                    .withMemorySwap(0L)
            )
            .withExposedPorts(6379)
            .withEnv("maxmemory", "256mb")
            .withEnv("maxmemory-policy", "allkeys-lru");

    @DynamicPropertySource
    protected static void mongoProperties(DynamicPropertyRegistry reg) {
        reg.add("spring.data.mongodb.uri", () -> {
            return MONGO_CONTAINER.getReplicaSetUrl();
        });
    }

    @DynamicPropertySource
    protected static void redisProperties(DynamicPropertyRegistry reg) {
        reg.add("spring.data.redis.host", () -> {
            var ip = REDIS.getContainerIpAddress();
            return ip;
        });
    }

    @AfterEach
    protected void cleanupAllDataInDb() {
        StepVerifier
                .create(mongo.getCollectionNames()
                        .flatMap(col -> mongo.remove(new Query(), col))
                        .collectList()
                )
                .expectNextCount(1L)
                .verifyComplete();
    }
}
