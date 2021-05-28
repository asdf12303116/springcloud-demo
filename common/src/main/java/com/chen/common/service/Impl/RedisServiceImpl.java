package com.chen.common.service.Impl;

import cn.hutool.core.util.StrUtil;
import com.chen.common.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    final Long DEFAULT_TIMEOUT = 1800L;
    final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.SECONDS;


    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public void put(String key, Object value) {
        put(key, value, DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
    }

    @Override
    public void put(String key, Object value, long timeout, TimeUnit unit) {
        put(null, key, value, timeout, unit);
    }

    @Override
    public void put(String namespace, String key, Object value) {
        put(namespace, key, value, DEFAULT_TIMEOUT, DEFAULT_TIMEUNIT);
    }

    @Override
    public void put(String namespace, String key, Object value, long timeout, TimeUnit unit) {
        String targetKey = "";
        TimeUnit timeUnit = DEFAULT_TIMEUNIT;
        if (StrUtil.isNotEmpty(namespace)) {
            targetKey = namespace + ":" + key;
        } else {
            targetKey = key;
        }
        if (Objects.nonNull(unit)) {
            timeUnit = unit;
        }
        redisTemplate.opsForValue().set(targetKey, value, timeout, timeUnit);

    }

    @Override
    public <T> T get(String key) {
        Object targetObject = redisTemplate.opsForValue().get(key);
        if (Objects.nonNull(targetObject)) {
            return (T) targetObject;

        }

        return null;
    }

    @Override
    public <T> T get(String namespace, String key) {
        String targetKey = "";
        if (StrUtil.isNotEmpty(namespace)) {
            targetKey = namespace + ":" + key;
            return get(targetKey);

        }
        return null;
    }

    @Override
    public void remove(String key) {
        if (Objects.nonNull(key)) {
            redisTemplate.delete(key);
        }

    }

    @Override
    public void remove(String namespace, String key) {
        String targetKey = "";
        if (StrUtil.isEmpty(namespace)) {
            targetKey = namespace + ":" + key;
            remove(targetKey);

        }
    }


    @Override
    public Set<String> keys() {
        return redisTemplate.keys("*");
    }

    @Override
    public Set<String> keys(String namespace) {
        if (null == namespace) {
            namespace = "";
        }
        //无值返回[]非null
        return redisTemplate.keys(namespace + ":*");
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean exists(String namespace, String key) {
        String targetKey = "";
        if (StrUtil.isNotEmpty(namespace)) {
            targetKey = namespace + ":" + key;
            return exists(targetKey);

        }
        return false;
    }
}
