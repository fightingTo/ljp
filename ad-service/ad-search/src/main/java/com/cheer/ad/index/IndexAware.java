package com.cheer.ad.index;

/**
 * 索引的增删改查
 * K - V : 索引的键和值
 *
 * @Created by ljp on 2019/11/19.
 */
public interface IndexAware<K, V> {

    V get(K key);

    void add(K key, V value);

    void update(K key, V value);

    void delete(K key, V value);
}
