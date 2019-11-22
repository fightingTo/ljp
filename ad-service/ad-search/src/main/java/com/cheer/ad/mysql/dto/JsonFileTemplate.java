package com.cheer.ad.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 映射模板文件
 *
 * @Created by ljp on 2019/11/22.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonFileTemplate {

    private String database;
    private List<JsonTable> tableList;
}
