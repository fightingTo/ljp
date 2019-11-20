package com.cheer.ad.index.creative;

import com.cheer.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创意索引实现
 *
 * @Created by ljp on 2019/11/20.
 */
@Component
@Slf4j
public class CreativeIndex implements IndexAware<Long, CreativeObject> {
    /**
     * 正向索引映射：id -> creative
     */
    private static Map<Long, CreativeObject> creativeMap;

    static {
        creativeMap = new ConcurrentHashMap<>();
    }

    @Override
    public CreativeObject get(Long creativeId) {
        return creativeMap.get(creativeId);
    }

    @Override
    public void add(Long creativeId, CreativeObject creative) {
        log.info("before add: {}", creativeMap);
        creativeMap.put(creativeId, creative);
        log.info("after add: {}", creativeMap);
    }

    @Override
    public void update(Long creativeId, CreativeObject creative) {
        log.info("before update: {}", creativeMap);

        CreativeObject oldObject = creativeMap.get(creativeId);
        if (null == oldObject) {
            creativeMap.put(creativeId, creative);
        } else {
            oldObject.update(creative);
        }

        log.info("after update: {}", creativeMap);
    }

    @Override
    public void delete(Long creativeId, CreativeObject creative) {
        log.info("before delete: {}", creativeMap);
        creativeMap.remove(creativeId);
        log.info("after delete: {}", creativeMap);
    }
}
