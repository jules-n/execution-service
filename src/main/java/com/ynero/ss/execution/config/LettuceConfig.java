package com.ynero.ss.execution.config;

import com.ynero.ss.execution.domain.Node;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import services.CacheService;
import services.RedisWithPrefixOptionCacheServiceImpl;

@Configuration
public class LettuceConfig {

    @Setter(onMethod_ = @Value("${spring.redis.host}"))
    private String hostName;

    @Setter(onMethod_ = @Value("${spring.redis.port}"))
    private int port;

    @Bean
    public RedisTemplate<String, Node> redisTemplate() {
        RedisTemplate<String, Node> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.json());
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        return template;
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        var lettuceConnectionFactory = new LettuceConnectionFactory();
        lettuceConnectionFactory.getStandaloneConfiguration()
                .setHostName(hostName);
        lettuceConnectionFactory.getStandaloneConfiguration()
                .setPort(port);
        lettuceConnectionFactory.setTimeout(60000);
        return lettuceConnectionFactory;
    }

    @Bean
    public CacheService<String, Node> cacheService() {
        return new RedisWithPrefixOptionCacheServiceImpl<String, Node>(redisTemplate(), "node: ");
    }
}
