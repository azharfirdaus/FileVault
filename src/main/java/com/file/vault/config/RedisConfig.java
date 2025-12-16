package com.file.vault.config;

import com.file.vault.config.serializer.ByteArrayRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

//    @Value("${spring.data.redis.timeout.ms}")
//    private long ttlMs;

    @Bean
    public RedisTemplate<String, byte[]> redisBinaryTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new ByteArrayRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

}
