package com.cheer.ad.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态标识
 *
 * @Created by ljp on 2019/11/14.
 */
@Getter
@AllArgsConstructor
public enum  CommonStatus {

    VALID(1, "有效状态"),
    INVALID(0, "无效状态");

    private Integer status;
    private String desc;
}
