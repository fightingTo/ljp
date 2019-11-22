package com.cheer.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义异常错误集合类
 *
 * @Created by ljp on 2019/11/19.
 */
@AllArgsConstructor
public enum CommonError {

    REQUEST_PARAM_ERROR("10001","请求参数错误"),
    SAME_NAME_ERROR("10002","存在同名的用户"),
    CAN_NOT_FIND_RECORD( "10003","找不到数据记录"),
    SAME_NAME_PLAN_ERROR( "10004","存在同名的推广计划"),
    SAME_NAME_UNIT_ERROR( "10005","存在同名的推广单元"),
    REQUEST_DATA_NUMBER_ERROR ( "10006","请求参数中的id获取对应数据库中的数据个数不符"),
    GET_DATA_EMPTY("10007", "获取数据为空"),
    WRITE_FILE_DATA_ERROR("10008", "写入索引文件数据出错"),
    READ_FILE_DATA_ERROR("10009", "加载索引文件数据出错"),
    PARSER_DATE_ERROR("20001", "解析字符串为时间错误");

    @Getter
    private String code;

    @Getter
    private String msg;
}
