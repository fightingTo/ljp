package com.cheer.ad.index.unit;

import com.cheer.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 推广单元索引实现
 * 构建正向索引，ID -> 数据
 *
 * @Created by ljp on 2019/11/20.
 */
@Component
@Slf4j
public class AdUnitIndex implements IndexAware<Long, AdUnitObject> {

    /**
     * 构建映射广告单元索引的map
     */
    private static Map<Long, AdUnitObject> indexUnitMap;

    static {
        indexUnitMap = new ConcurrentHashMap<>();
    }

    @Override
    public AdUnitObject get(Long key) {
        return indexUnitMap.get(key);
    }

    @Override
    public void add(Long key, AdUnitObject value) {
        log.info("before add: {}", indexUnitMap);
        indexUnitMap.put(key, value);
        log.info("after add: {}", indexUnitMap);
    }

    @Override
    public void update(Long key, AdUnitObject value) {
        log.info("before update: {}", indexUnitMap);

        AdUnitObject oldObject = indexUnitMap.get(key);
        if (null == oldObject) {
            indexUnitMap.put(key, value);
        } else {
            oldObject.update(value);
        }

        log.info("after update: {}", indexUnitMap);
    }

    @Override
    public void delete(Long key, AdUnitObject value) {
        log.info("before delete: {}", indexUnitMap);
        indexUnitMap.remove(key);
        log.info("after delete: {}", indexUnitMap);
    }
}
