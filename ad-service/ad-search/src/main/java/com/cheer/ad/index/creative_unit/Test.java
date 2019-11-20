package com.cheer.ad.index.creative_unit;

import org.apache.commons.collections4.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 测试
 *
 * @Created by ljp on 2019/11/20.
 */
public class Test {

    public static void main(String[] args) {

        /*
         * value.getAdId() ---> "1"
         * value.getUnitId() --> 2L
         */
        Map<String, Set<Long>> creativeUnitMap = new HashMap<>();
        Set<Long> unitSet = creativeUnitMap.get("1");
        if (CollectionUtils.isEmpty(unitSet)) {
            unitSet = new ConcurrentSkipListSet<>();
            creativeUnitMap.put("1", unitSet);
        }
        unitSet.add(2L);
        creativeUnitMap.forEach((k, v) ->
                System.out.println(k + ": " + v)
        );
    }
}
