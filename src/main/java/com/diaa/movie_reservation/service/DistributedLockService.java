package com.diaa.movie_reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DistributedLockService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX = "lock:";

    public boolean acquireLock(String key, long ttl, TimeUnit unit) {
        return Boolean.TRUE.equals(
                redisTemplate.opsForValue().setIfAbsent(PREFIX + key, "locked", ttl, unit)
        );
    }

    public void releaseLock(String key) {
        redisTemplate.delete(PREFIX + key);
    }
}