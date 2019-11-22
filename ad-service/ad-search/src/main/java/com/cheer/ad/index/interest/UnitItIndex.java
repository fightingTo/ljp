package com.cheer.ad.index.interest;

import com.cheer.ad.index.IndexAware;
import com.cheer.ad.utils.IndexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 兴趣关联单元索引实现
 * 正向索引及倒排索引
 *
 * @Created by ljp on 2019/11/20.
 */
@Slf4j
@Component
public class UnitItIndex implements IndexAware<String, Set<Long>> {

    /**
     * 倒排索引：tag -> unitIds
     */
    private static Map<String, Set<Long>> itUnitMap;

    /**
     * 正向索引：unitId -> tags
     */
    private static Map<Long, Set<String>> unitItMap;

    static {
        itUnitMap = new ConcurrentHashMap<>();
        unitItMap = new ConcurrentHashMap<>();
    }


    @Override
    public Set<Long> get(String tag) {
        return itUnitMap.get(tag);
    }

    @Override
    public void add(String tag, Set<Long> unitIds) {
        log.info("UnitItIndex, before add: {}", unitItMap);
        Set<Long> unitIdSet = IndexUtils.getAndCreateIfNeed(
                tag,
                itUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIdSet.addAll(unitIds);

        for (Long unitId : unitIds) {
            Set<String> tagSet = IndexUtils.getAndCreateIfNeed(
                    unitId,
                    unitItMap,
                    ConcurrentSkipListSet::new
            );
            tagSet.add(tag);
        }
        log.info("UnitItIndex, after add: {}", unitItMap);
    }

    @Override
    public void update(String tag, Set<Long> value) {
        log.error("it index can not support update");
    }

    /**
     * key对应索引map中的value有可能不是全量索引数据
     * 所以需要先通过key获取到保存索引的值，然后再从其删除传入的unitIds
     * 支持部分删除
     */
    @Override
    public void delete(String tag, Set<Long> unitIds) {
        Set<Long> unitIdSet = IndexUtils.getAndCreateIfNeed(
                tag,
                itUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIdSet.removeAll(unitIds);

        for (Long unitId : unitIds) {
            Set<String> tagSet = IndexUtils.getAndCreateIfNeed(
                    unitId,
                    unitItMap,
                    ConcurrentSkipListSet::new
            );
            tagSet.remove(tag);
        }
        log.info("UnitItIndex, after delete: {}", unitItMap);
    }

    /**
     * 判断推广单元是否包含了传入的兴趣标签
     *
     * @param unitId 推广单元id
     * @param itTags 兴趣标签
     * @return 当推广单元关联的兴趣标签集合包含了所有的兴趣标签时，返回true
     */
    public static boolean isMatchTags(Long unitId, List<String> itTags){
        if(unitItMap.containsKey(unitId) && CollectionUtils.isNotEmpty(unitItMap.get(unitId))) {
            Set<String> itTagSet = unitItMap.get(unitId);
            return CollectionUtils.isSubCollection(itTags, itTagSet);
        }
        return false;
    }
}
