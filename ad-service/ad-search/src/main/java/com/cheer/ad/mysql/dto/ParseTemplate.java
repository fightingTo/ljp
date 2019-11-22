package com.cheer.ad.mysql.dto;

import com.cheer.ad.mysql.constant.OperationType;
import com.cheer.ad.utils.IndexUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析模板文件
 *
 * @Created by ljp on 2019/11/22.
 */
@Data
public class ParseTemplate {

    private String database;

    /**
     * key: 数据库名称
     * value: 表数据
     */
    private Map<String, TableTemplate> tableTemplateMap = new HashMap<>();

    /**
     * 解析json模板文件数据
     *
     * @param jsonFileTemplate json模板文件映射对象
     * @return 解析后封装的对象
     */
    public static ParseTemplate parse(JsonFileTemplate jsonFileTemplate) {
        ParseTemplate parseTemplate = new ParseTemplate();

        // 设置数据库名称
        parseTemplate.setDatabase(jsonFileTemplate.getDatabase());

        // 构造TableTemplate对象
        TableTemplate tableTemplate = new TableTemplate();

        // 设置TableTemplate对象属性值
        for (JsonTable table : jsonFileTemplate.getTableList()) {
            tableTemplate.setLevel(table.getLevel().toString());
            tableTemplate.setTableName(table.getTableName());

            // 设置OperationTypeListMap值
            Map<OperationType, List<String>> operationTypeListMap = tableTemplate.getOperationTypeListMap();

            // 监听插入数据增量的改变
            for (JsonTable.Column insertColumn : table.getInsert()) {
                List<String> addColumns = IndexUtils.getAndCreateIfNeed(
                        OperationType.ADD,
                        operationTypeListMap,
                        ArrayList::new
                );
                addColumns.add(insertColumn.getColumn());
            }

            // 监听修改数据增量的改变
            for (JsonTable.Column updateColumn : table.getUpdate()) {
                List<String> updateColumns = IndexUtils.getAndCreateIfNeed(
                        OperationType.UPDATE,
                        operationTypeListMap,
                        ArrayList::new
                );
                updateColumns.add(updateColumn.getColumn());
            }

            // 监听删除数据增量的改变
            for (JsonTable.Column deleteColumn : table.getDelete()) {
                List<String> deleteColumns = IndexUtils.getAndCreateIfNeed(
                        OperationType.UPDATE,
                        operationTypeListMap,
                        ArrayList::new
                );
                deleteColumns.add(deleteColumn.getColumn());
            }

            // 设置operationTypeListMap值
            tableTemplate.setOperationTypeListMap(operationTypeListMap);
        }

        // 设置parseTemplate属性
        parseTemplate.tableTemplateMap.put(jsonFileTemplate.getDatabase(), tableTemplate);

        return parseTemplate;
    }

}
