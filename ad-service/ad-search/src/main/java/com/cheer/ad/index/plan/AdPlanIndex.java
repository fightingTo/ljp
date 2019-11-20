package com.cheer.ad.index.plan;

import com.cheer.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 推广计划索引实现类
 * 构建正向索引，ID -> 数据
 *
 * @Created by ljp on 2019/11/20.
 */
@Component
@Slf4j
public class AdPlanIndex implements IndexAware<Long, AdPlanObject> {
    /**
     * 构建映射广告计划索引的map
     */
    private static Map<Long, AdPlanObject> indexPlanMap;

    static {
        indexPlanMap = new ConcurrentHashMap<>();
    }

    @Override
    public AdPlanObject get(Long key) {
        return indexPlanMap.get(key);
    }

    @Override
    public void add(Long key, AdPlanObject value) {
        log.info("before add: {}", indexPlanMap);
        indexPlanMap.put(key, value);
        log.info("after add: {}", indexPlanMap);
    }

    @Override
    public void update(Long key, AdPlanObject value) {
        log.info("before update: {}", indexPlanMap);

        AdPlanObject oldObject = indexPlanMap.get(key);
        if (null == oldObject) {
            indexPlanMap.put(key, value);
        } else {
            oldObject.update(value);
        }

        log.info("after update: {}", indexPlanMap);
    }

    @Override
    public void delete(Long key, AdPlanObject value) {
        log.info("before delete: {}", indexPlanMap);
        indexPlanMap.remove(key);
        log.info("after delete: {}", indexPlanMap);
    }
}
