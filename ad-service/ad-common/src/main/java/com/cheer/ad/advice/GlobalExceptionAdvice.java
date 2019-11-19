package com.cheer.ad.advice;

import com.cheer.ad.exception.AdException;
import com.cheer.ad.vo.CommonConstants;
import com.cheer.ad.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理
 *
 * @Created by ljp on 2019/11/14.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public CommonResponse<String> handlerAdException(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     Exception ex){

        //系统级异常，错误码固定为-1，提示语固定为系统繁忙，请稍后再试
        CommonResponse<String> result = new CommonResponse<>(
                -1,
                CommonConstants.ErrorMsg.SYSTEM_EXCEPTION
        );

        //如果是业务逻辑异常，返回具体的错误码与提示信息
        if (ex instanceof AdException) {
            AdException adException = (AdException) ex;
//            result.setCode(Integer.parseInt(adException.getCode()));
//            result.setMessage(adException.getErrorMsg());
            result.setCode(Integer.parseInt(adException.getResponse().getCode()));
            result.setMessage(adException.getResponse().getMsg());
        } else {
            //对系统级异常进行日志记录
            log.error("系统异常:" + ex.getMessage(), ex);
        }

        return result;
    }
}
