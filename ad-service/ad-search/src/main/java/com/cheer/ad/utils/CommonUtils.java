package com.cheer.ad.utils;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 索引工具类
 *
 * @Created by ljp on 2019/11/20.
 */
public class CommonUtils {
    /**
     * 对传入进来的map的key值操作
     *    -> 存在，返回存在的值
     *    -> 不存在，返回factory构造的值
     *
     * @param key 传进来map的key
     * @param map 需要操作的map
     * @param factory 构造新值表达式
     * @param <V> 返回map操作后的值
     * @return map的value
     */
    public static <K, V> V getOrCreate(K key, Map<K, V> map, Supplier<V> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }

}
