package com.cheer.ad.exception;

import com.cheer.ad.vo.CommonError;

/**
 * 自定义异常用于统一异常处理
 *
 * @Created by ljp on 2019/11/14.
 */

public class AdException extends Exception {
//    /**
//     * 异常信息
//     */
//    @Setter
//    @Getter
//    private String errorMsg;
//    /**
//     * 错误码
//     */
//    @Setter
//    @Getter
//    private String code;

    private CommonError response;

    public AdException(CommonError response) {
//        super(errorMsg);
//        this.code = errorMsg.substring(0, 5);
//        this.errorMsg = errorMsg.substring(6);
        this.response = response;
    }

    public CommonError getResponse() {
        return response;
    }

}
