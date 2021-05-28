package com.chen.common.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public interface RedisService {
    /**
     * 在默认缓存空间内加入和更新缓存信息
     *
     * @param key   keyID
     * @param value 值
     */
    void put(String key, Object value);


    /**
     * 在默认缓存空间内加入和更新缓存信息
     *
     * @param key     keyID
     * @param value   值
     * @param timeout 有效时长
     * @param unit    有效时长单位
     */
    void put(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 在指定缓存空间内加入或更新缓存信息
     *
     * @param namespace 缓存空间名称
     * @param key       keyID
     * @param value     值
     */
    void put(String namespace, String key, Object value);

    /**
     * 在指定缓存空间内加入或更新缓存信息
     *
     * @param namespace 缓存空间名称
     * @param key       keyID
     * @param value     值
     * @param timeout   有效时长
     * @param unit      有效时长单位
     */
    void put(String namespace, String key, Object value, long timeout, TimeUnit unit);

    /**
     * 在默认缓存空间内获取缓存信息
     * 如果信息不存在这遍历其他缓存空间
     *
     * @param key keyID
     * @return 缓存值对象
     */
    <T> T get(String key);

    /**
     * 获取缓存信息
     *
     * @param namespace 缓存空间名称
     * @param key       keyID
     */
    <T> T get(String namespace, String key);

    /**
     * 在所有缓存空间删除缓存信息
     *
     * @param key keyID
     */
    void remove(String key);

    /**
     * 在指定空间内删除缓存信息
     *
     * @param namespace 缓存空间名称
     * @param key       keyID
     */
    void remove(String namespace, String key);


    /**
     * 获取所有keys
     *
     * @return 所有KeyId
     */
    Set<String> keys();

    /**
     * 获取指定空间所有keys
     *
     * @param namespace 缓存空间名称
     * @return 所有KeyId
     */
    Set<String> keys(String namespace);


}
