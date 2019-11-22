package com.cheer.ad.handler;

import com.alibaba.fastjson.JSON;
import com.cheer.ad.dump.AdCreativeTable;
import com.cheer.ad.dump.AdCreativeUnitTable;
import com.cheer.ad.dump.AdPlanTable;
import com.cheer.ad.dump.AdUnitDistrictTable;
import com.cheer.ad.dump.AdUnitItTable;
import com.cheer.ad.dump.AdUnitKeywordTable;
import com.cheer.ad.dump.AdUnitTable;
import com.cheer.ad.index.DataTable;
import com.cheer.ad.index.IndexAware;
import com.cheer.ad.index.creative.CreativeIndex;
import com.cheer.ad.index.creative.CreativeObject;
import com.cheer.ad.index.creative_unit.CreativeUnitIndex;
import com.cheer.ad.index.creative_unit.CreativeUnitObject;
import com.cheer.ad.index.interest.UnitItIndex;
import com.cheer.ad.index.keyword.UnitKeywordIndex;
import com.cheer.ad.index.plan.AdPlanIndex;
import com.cheer.ad.index.plan.AdPlanObject;
import com.cheer.ad.index.unit.AdUnitIndex;
import com.cheer.ad.index.unit.AdUnitObject;
import com.cheer.ad.mysql.constant.OperationType;
import com.cheer.ad.utils.IndexUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 索引数据处理
 * 1. 索引之间存在着层级的划分, 也就是依赖关系的划分
 * 2. 加载全量索引其实是增量索引 "添加" 的一种特殊实现
 *
 * @Created by ljp on 2019/11/21.
 */
@Slf4j
public class AdLevelDataHandler {

    /**
     * 第二层级索引操作实现：plan, creative
     */
    public static void handleLevel2(AdPlanTable planTable, OperationType type){
        AdPlanObject planObject = AdPlanObject.builder()
                .planId(planTable.getId())
                .userId(planTable.getUserId())
                .planStatus(planTable.getPlanStatus())
                .startDate(planTable.getStartDate())
                .endDate(planTable.getEndDate())
                .build();
        handleBinlogEvent(DataTable.of(AdPlanIndex.class), planObject.getPlanId(), planObject,type);
    }

    public static void handleLevel2(AdCreativeTable creativeTable, OperationType type){
        CreativeObject creativeObject = CreativeObject.builder()
                .adId(creativeTable.getCreativeId())
                .adUrl(creativeTable.getAdUrl())
                .auditStatus(creativeTable.getAuditStatus())
                .height(creativeTable.getHeight())
                .materialType(creativeTable.getMaterialType())
                .name(creativeTable.getName())
                .type(creativeTable.getType())
                .width(creativeTable.getWidth())
                .build();
        handleBinlogEvent(DataTable.of(CreativeIndex.class), creativeObject.getAdId(), creativeObject, type);
    }

    /**
     * 第三层级索引操作实现：unit, creative_unit
     */
    public static void handleLevel3(AdUnitTable unitTable, OperationType type){
        // 获取关联计划
        AdPlanObject planObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());
        if (planObject == null) {
            log.error("unit has no associated plan, planId:{}", unitTable.getPlanId());
            return;
        }

        AdUnitObject unitObject = AdUnitObject.builder()
                .adPlanObject(planObject)
                .planId(unitTable.getPlanId())
                .positionType(unitTable.getPositionType())
                .unitId(unitTable.getUnitId())
                .unitStatus(unitTable.getUnitStatus())
                .build();

        handleBinlogEvent(DataTable.of(AdUnitIndex.class), unitObject.getUnitId(), unitObject, type);
    }

    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OperationType type){
        //TODO: 不支持更新操作(why???)
        if (type == OperationType.UPDATE) {
            log.error("creative associated unit no support update operation");
            return;
        }

        // 获取关联对象
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getCreativeId());
        if (unitObject == null || creativeObject == null) {
            log.error("AdCreativeUnitTable index error, creativeUnitTable:{}", JSON.toJSONString(creativeUnitTable));
            return;
        }
        CreativeUnitObject creativeUnitObject = CreativeUnitObject.builder()
                .unitId(creativeUnitTable.getUnitId())
                .creativeId(creativeUnitTable.getCreativeId()).build();

        // 获取CreativeUnitIndex索引key
        String concatId = IndexUtils.concat(
                creativeUnitObject.getCreativeId().toString(),
                creativeUnitObject.getUnitId().toString()
        );

        handleBinlogEvent(DataTable.of(CreativeUnitIndex.class), concatId, creativeUnitObject, type);
    }

    /**
     * 第四层级索引操作实现：unit_it, unit_district, unit_keyword
     */
    public static void handleLevel4(AdUnitItTable itTable, OperationType type){
        if (type == OperationType.UPDATE) {
            log.error("it index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(itTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitItTable index error: {}", itTable.getUnitId());
            return;
        }

        Set<Long> unitIds = new HashSet<>(Collections.singleton(itTable.getUnitId()));

        handleBinlogEvent(DataTable.of(UnitItIndex.class), itTable.getItTag(), unitIds, type);
    }

    public static void handleLevel4(AdUnitDistrictTable districtTable, OperationType type){
        if (type == OperationType.UPDATE) {
            log.error("district index can not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(districtTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitDistrictTable index error, unitId:{}", districtTable.getUnitId());
            return;
        }

        // 获取地域索引key -> province-city
        String concatKey = IndexUtils.concat(districtTable.getProvince(), districtTable.getCity());

        //获取地域索引value -> unitIds
        Set<Long> unitIds = new HashSet<>(Collections.singleton(districtTable.getUnitId()));

        handleBinlogEvent(DataTable.of(UnitItIndex.class), concatKey, unitIds, type);
    }

    public static void handleLevel4(AdUnitKeywordTable keywordTable, OperationType type){
        if (type == OperationType.UPDATE) {
            log.error("keyword index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(keywordTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitKeywordTable index error, unitId:{}", keywordTable.getUnitId());
        }

        // 获取关键词索引value
        Set<Long> unitIds = new HashSet<>(Collections.singleton(keywordTable.getUnitId()));

        handleBinlogEvent(DataTable.of(UnitKeywordIndex.class), keywordTable.getKeyword(), unitIds, type);
    }

    /**
     * 监听mysql-binlog增量数据，进行索引操作
     */
    private static <K, V> void handleBinlogEvent(IndexAware<K, V> index, K key, V value, OperationType type) {
        switch (type) {
            case ADD:
                index.add(key, value);
                break;
            case UPDATE:
                index.update(key, value);
                break;
            case DELETE:
                index.delete(key, value);
                break;
            default:
                break;
        }
    }
}
