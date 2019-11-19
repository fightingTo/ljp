package com.cheer.ad.vo;

/**
 * 错误提示集合类
 *
 * @Created by ljp on 2019/11/19.
 */
public class CommonConstants {
    public static class ErrorMsg {

        /**
         * 系统错误，返回信息，对应状态码为-1
         */
        public static final String SYSTEM_EXCEPTION = "系统繁忙，请稍后再试";

        /**
         * 业务逻辑错误
         */
        public static final String REQUEST_PARAM_ERROR = "10001_请求参数错误";
        public static final String SAME_NAME_ERROR = "10002_存在同名的用户";
        public static final String CAN_NOT_FIND_RECORD = "10003_找不到数据记录";
        public static final String SAME_NAME_PLAN_ERROR = "10004_存在同名的推广计划";
        public static final String SAME_NAME_UNIT_ERROR = "10005_存在同名的推广单元";
        public static final String REQUEST_DATA_NUMBER_ERROR = "10006_请求参数中的id获取对应数据库中的数据个数不符";

    }
}
