package com.cfgglobal.test.cache;


import java.util.Set;
import java.util.function.Supplier;

public interface CacheClient {

    <T> T get(String key);

    <T> T get(String key, Supplier<T> supplier);

    <T> T get(String key, Supplier<T> supplier, long expire);

    void set(String key, Object value);


    void set(String key, long expire, Object value);

    void deleteByKey(String key);

    void deleteByPattern(String key);

    Set<String> keys(String key);


}
