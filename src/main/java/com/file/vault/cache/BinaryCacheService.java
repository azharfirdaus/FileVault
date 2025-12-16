package com.file.vault.cache;

import com.file.vault.client.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BinaryCacheService {

    private static final Logger logger = LoggerFactory.getLogger(BinaryCacheService.class);

    private static final long MAX_CACHE_SIZE_BYTES = 2 * 1024 * 1024;

    private final RedisClient redisClient;

    public BinaryCacheService(@Autowired RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Async("cacheExecutor")
    public void saveCache(UUID uuid, byte[] data) {
        if(data.length > MAX_CACHE_SIZE_BYTES){
            logger.debug("skip save cache uuid: {}, file too large", uuid.toString());
            return;
        }
        try {
            redisClient.put(uuid.toString(), data);
        } catch (Exception e) {
            logger.error("Failed to save cache uuid: {}, error: {}", uuid.toString(), e.getMessage());
        }
    }

    public byte[] getCache(UUID uuid){
        try {
            return redisClient.get(uuid.toString());
        } catch (Exception e) {
            logger.error("Failed to get cache uuid: {}, error: {}", uuid.toString(), e.getMessage());
            return null;
        }
    }

    @Async("cacheExecutor")
    public void removeCacheAsync(UUID uuid) {
        try {
            redisClient.delete(uuid.toString());
        } catch (Exception e) {
            logger.error("Failed to remove cache uuid: {}, error: {}", uuid.toString(), e.getMessage());
        }
    }
}
