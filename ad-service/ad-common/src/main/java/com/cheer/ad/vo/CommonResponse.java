package com.cheer.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应实体类
 *
 * @Created by ljp on 2019/11/14.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> implements Serializable {
    /**
     * 0 代表正常码
     * -1 代表系统异常码
     * 其余代表各逻辑异常码
     */
    private Integer code;

    private String message;

    private T data;

    public CommonResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
