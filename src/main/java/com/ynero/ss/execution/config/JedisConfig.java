package com.ynero.ss.execution.config;

import com.ynero.ss.execution.domain.Node;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import services.CacheService;
import services.RedisWithPrefixOptionCacheServiceImpl;

@Configuration
@Log4j2
public class JedisConfig {

    @Setter(onMethod_ = @Value("${spring.redis.host}"))
    private String hostName;

    @Setter(onMethod_ = @Value("${spring.redis.port}"))
    private int port;

    @Bean
    public RedisTemplate<String, Node> redisTemplate() {
        RedisTemplate<String, Node> template = new RedisTemplate<>();
        var factory = jedisConnectionFactory();
        var connection = factory.getConnection();
        if (!connection.isSubscribed()) {
            log.info("connection failed: {}", connection);
        }
        template.setConnectionFactory(factory);
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.json());
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        return template;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(hostName, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public CacheService<String, Node> cacheService() {
        return new RedisWithPrefixOptionCacheServiceImpl<String, Node>(redisTemplate(), "node: ");
    }
}