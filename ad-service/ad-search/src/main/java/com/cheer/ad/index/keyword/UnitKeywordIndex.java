package com.cheer.ad.index.keyword;

import com.cheer.ad.index.IndexAware;
import com.cheer.ad.utils.IndexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 推广单元关联关键词索引实现类
 * 构建倒排索引，关键词 -> UNIT_IDS
 *     正向索引，unitId -> 关键词集合
 *
 * @Created by ljp on 2019/11/20.
 */
@Slf4j
@Component
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {
    /**
     * 关键词 -> unitIds映射
     */
    private static Map<String, Set<Long>> keywordUnitMap;

    /**
     * unitId -> 关键词集合映射
     */
    private static Map<Long, Set<String>> unitKeywordMap;

    static {
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        if (StringUtils.isEmpty(key)) {
            return Collections.emptySet();
        }

        Set<Long> result = keywordUnitMap.get(key);
        if (result == null) {
            return Collections.emptySet();
        }
        return result;
    }

    @Override
    public void add(String keyword, Set<Long> unitIds) {
        log.info("UnitKeywordIndex, before add: {}", unitKeywordMap);

        // 对不存关键字的keywordUnitMap中的key，对应构建一个set集合
        Set<Long> unitIdSet = IndexUtils.getAndCreateIfNeed(
                keyword,
                keywordUnitMap,
                ConcurrentSkipListSet::new
        );
        // 将需要添加的unitIds放入构建的set集合中
        unitIdSet.addAll(unitIds);

        // 添加unitId -> 关键词集合索引
        for (Long unitId : unitIds) {

            /*
             * 某个推广单元还不存在需要添加的关键词
             *      -> 构建并添加此推广单元对应不存在关键词的集合
             */
            Set<String> keywordSet = IndexUtils.getAndCreateIfNeed(unitId, unitKeywordMap, ConcurrentSkipListSet::new
            );
            keywordSet.add(keyword);
        }

        log.info("UnitKeywordIndex, after add: {}", unitKeywordMap);
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("keyword index can not support update");
    }

    @Override
    public void delete(String keyword, Set<Long> unitIds) {
        log.info("UnitKeywordIndex, before delete: {}", unitKeywordMap);

        // 可能删除的是部分关键词 -> unitIds的映射，先获取
        Set<Long> unitIdSet = IndexUtils.getAndCreateIfNeed(
                keyword,
                keywordUnitMap,
                ConcurrentSkipListSet::new
        );
        unitIdSet.removeAll(unitIds);

        for (Long unitId : unitIds) {
            Set<String> keywordSet = IndexUtils.getAndCreateIfNeed(
                    unitId, unitKeywordMap,
                    ConcurrentSkipListSet::new
            );
            keywordSet.remove(keyword);
        }

        log.info("UnitKeywordIndex, after delete: {}", unitKeywordMap);
    }

    /**
     * 匹配推广单元是否包含了传入的关键词集合
     *
     * @param unitId 推广单元id
     * @param keywords 关联推广单元的关键词集合
     * @return 当推广单元关联的关键词集合包含了所有的关键词限制时，返回true
     */
    public boolean matchKeywords(Long unitId, List<String> keywords) {
        if (unitKeywordMap.containsKey(unitId)
                && CollectionUtils.isNotEmpty(unitKeywordMap.get(unitId))) {

            // 索引中推广单元对应的关键词集合
            Set<String> unitKeywords = unitKeywordMap.get(unitId);

            // 判断传入进来的关键词集合是否包含于索引中关键词集合
            return CollectionUtils.isSubCollection(keywords, unitKeywords);
        }
        return false;
    }
}