package com.cfgglobal.test.cache;

import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static io.vavr.API.*;
import static io.vavr.Patterns.$Failure;
import static io.vavr.Patterns.$Success;

@Component
public class RedisCacheClient implements CacheClient {
    private static Logger logger = LoggerFactory.getLogger(RedisCacheClient.class);

    @Autowired
    private RedisTemplate<String, Object> template;


    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) template.opsForValue().get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Supplier<T> supplier) {
        Try<Object> cache = Try.of(() -> template.opsForValue().get(key));
        Optional value = Match(cache).of(
                Case($Success($()), Optional::ofNullable),
                Case($Failure($()), result -> {
                    logger.error("Query key [{}] from cache failure", key, result.getCause());
                    return Optional.empty();
                })
        );
        return (T) value.orElseGet(() -> {
            T val = null;
            try {
                val = supplier.get();
            } catch (Exception e) {
                logger.error("cannot find cached configuration {}, {}", key, e.getMessage());
            }

            logger.debug("Query key [{}] from cache success, value [{}] ", key, val);
            template.opsForValue().set(key, val);
            return val;
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Supplier<T> supplier, long expire) {
        Object value = get(key, supplier);
        template.expire(key, expire, TimeUnit.SECONDS);
        return (T) value;
    }


    @Override
    public void set(String key, Object value) {
        template.opsForValue().set(key, value);
    }


    @Override
    public void set(String key, long expire, Object value) {
        set(key, value);
        template.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public void deleteByKey(String key) {
        template.delete(key);
    }

    @Override
    public void deleteByPattern(String pattern) {
        Set<String> keys = template.keys(pattern);
        for (String key : keys) {
            logger.debug("Delete key [{}] in cache", key);
            template.delete(key);
        }
    }

    @Override
    public Set<String> keys(String pattern) {
        return template.keys(pattern);
    }


}
