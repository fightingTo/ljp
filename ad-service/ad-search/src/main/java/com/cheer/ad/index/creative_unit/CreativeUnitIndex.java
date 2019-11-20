package com.cheer.ad.index.creative_unit;

import com.cheer.ad.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 创意关联单元索引实现(中间表)
 *
 * @Created by ljp on 2019/11/20.
 */
@Component
@Slf4j
public class CreativeUnitIndex implements IndexAware<String, CreativeUnitObject> {

    static {
        creativeMap = new ConcurrentHashMap<>();
        creativeUnitMap = new ConcurrentHashMap<>();
        unitCreativeMap = new ConcurrentHashMap<>();
    }

    /**
     * 索引映射结构：creativeId-unitId -> CreativeUnitObject
     */
    private static Map<String, CreativeUnitObject> creativeMap;

    /**
     * 索引映射结构：creativeId -> unitIds
     */
    private static Map<Long, Set<Long>> creativeUnitMap;

    /**
     * 索引映射结构：unitIds -> creativeId
     */
    private static Map<Long, Set<Long>> unitCreativeMap;



    @Override
    public CreativeUnitObject get(String compositeId) {
        return creativeMap.get(compositeId);
    }

    @Override
    public void add(String compositeId, CreativeUnitObject creativeUnit) {
        log.info("before add: {}", creativeMap);

        // 添加
        creativeMap.put(compositeId, creativeUnit);

        // 更新creativeUnitMap
        Set<Long> unitIdSet = creativeUnitMap.get(creativeUnit.getCreativeId());
        if (CollectionUtils.isEmpty(unitIdSet)) {
            unitIdSet = new ConcurrentSkipListSet<>();
        }
        unitIdSet.add(creativeUnit.getUnitId());
        // TODO: 这步更新操作是不是忘记了(真打脸)
        creativeUnitMap.put(creativeUnit.getCreativeId(), unitIdSet);

        // 更新unitCreativeMap
        Set<Long> creativeIdSet = unitCreativeMap.get(creativeUnit.getUnitId());
        if (CollectionUtils.isEmpty(creativeIdSet)) {
            creativeIdSet = new ConcurrentSkipListSet<>();
        }
        creativeIdSet.add(creativeUnit.getCreativeId());
        unitCreativeMap.put(creativeUnit.getUnitId(), creativeIdSet);

        log.info("after add: {}", creativeMap);
    }

    @Override
    public void update(String compositeId, CreativeUnitObject creativeUnit) {
        log.error("CreativeUnitIndex not support update");
    }

    @Override
    public void delete(String compositeId, CreativeUnitObject creativeUnit) {
        log.info("before delete: {}", creativeMap);

        creativeMap.remove(compositeId);

        // 更新creativeUnitMap
        Set<Long> unitIdSet = creativeUnitMap.get(creativeUnit.getCreativeId());
        if (CollectionUtils.isNotEmpty(unitIdSet)) {
            unitIdSet.remove(creativeUnit.getUnitId());
        }

        // 更新unitCreativeMap
        Set<Long> creativeIdSet = unitCreativeMap.get(creativeUnit.getUnitId());
        if (CollectionUtils.isNotEmpty(creativeIdSet)) {
            creativeIdSet.remove(creativeUnit.getCreativeId());
        }

        log.info("after delete: {}", creativeMap);
    }
}
