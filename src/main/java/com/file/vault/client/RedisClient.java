package com.file.vault.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisClient {

    private final RedisTemplate<String, byte[]> redis;
    private final Duration ttl;

    public RedisClient(RedisTemplate<String, byte[]> redis,
                       @Value("${spring.data.redis.timeout.ms}") long ttlMs) {
        this.redis = redis;
        this.ttl = Duration.ofMillis(ttlMs);
    }

    public void put(String key, byte[] data) {
        redis.opsForValue().set(key, data, ttl);
    }

    public byte[] get(String key) {
        return redis.opsForValue().get(key);
    }

    public void delete(String key) {
        redis.delete(key);
    }
}
