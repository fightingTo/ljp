package com.cheer.ad.mysql.dto;

import com.cheer.ad.mysql.constant.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表的映射对象
 *
 * @Created by ljp on 2019/11/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableTemplate {

    private String tableName;

    private String level;

    /**
     * 表经过某一操作（ADD,UPDATE,DELETE）后变更数据（List<String>）
     * binlog操作类型 -> 对应表中行改变的数据集合
     */
    private Map<OperationType, List<String>> operationTypeListMap = new HashMap<>();

    /**
     * binlog监控数据，字段索引 -> 字段名
     */
    private Map<Integer, String> posMap = new HashMap<>();

}
