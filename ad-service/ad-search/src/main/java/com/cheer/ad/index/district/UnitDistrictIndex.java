package com.cheer.ad.index.district;

import com.cheer.ad.index.IndexAware;
import com.cheer.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 地域关联单元索引实现
 *
 * @Created by ljp on 2019/11/20.
 */
@Component
@Slf4j
public class UnitDistrictIndex implements IndexAware<String, Set<Long>> {
    /**
     * 倒排索引：province-city -> unitIds
     */
    private static Map<String, Set<Long>> districtUnitMap;

    /**
     * 正向索引：unitId -> (province-city)集合
     */
    private static Map<Long, Set<String>> unitDistrictMap;

    static {
        districtUnitMap = new ConcurrentHashMap<>();
        unitDistrictMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String provinceAndCity) {
        return districtUnitMap.get(provinceAndCity);
    }

    @Override
    public void add(String provinceAndCity, Set<Long> unitIds) {
        log.info("UnitDistrictIndex, before add: {}", unitDistrictMap);

        // 先对districtUnitMap倒排索引进行添加
        Set<Long> unitIdSet = CommonUtils.getOrCreate(
                provinceAndCity,
                districtUnitMap,
                CopyOnWriteArraySet::new
        );
        unitIdSet.addAll(unitIds);

        // 对unitDistrictMap正向索引进行添加
        for (Long unitId : unitIds) {
            Set<String> provinceAndCitySet = CommonUtils.getOrCreate(
                    unitId,
                    unitDistrictMap,
                    CopyOnWriteArraySet::new
            );
            provinceAndCitySet.add(provinceAndCity);
        }

        log.info("UnitDistrictIndex, after add: {}", unitDistrictMap);
    }

    @Override
    public void update(String provinceAndCity, Set<Long> unitIds) {
        log.error("district index can not support update");
    }

    @Override
    public void delete(String provinceAndCity, Set<Long> unitIds) {
        log.info("UnitDistrictIndex, before delete: {}", unitDistrictMap);

        // 先对districtUnitMap倒排索引进行删除(支持部分删除)
        Set<Long> unitIdSet = CommonUtils.getOrCreate(
                provinceAndCity,
                districtUnitMap,
                CopyOnWriteArraySet::new
        );
        unitIdSet.removeAll(unitIds);

        // 对unitDistrictMap正向索引进行刪除
        for (Long unitId : unitIds) {
            Set<String> provinceAndCitySet = CommonUtils.getOrCreate(
                    unitId,
                    unitDistrictMap,
                    ConcurrentSkipListSet::new
            );
            provinceAndCitySet.remove(provinceAndCity);
        }

        log.info("UnitDistrictIndex, after delete: {}", unitDistrictMap);
    }

    // TODO : 未完成
    public static boolean isMatchDistrict(Long unitId, List<String> districts) {
        if (unitDistrictMap.containsKey(unitId) && CollectionUtils.isNotEmpty(unitDistrictMap.get(unitId))) {
            Set<String> districtSet = unitDistrictMap.get(unitId);
            return CollectionUtils.isSubCollection(districts, districtSet);
        }
        return false;
    }
}
