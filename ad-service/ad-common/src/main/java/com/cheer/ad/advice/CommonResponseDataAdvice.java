package com.cheer.ad.advice;

import com.cheer.ad.annotation.IgnoreResponseAdvice;
import com.cheer.ad.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一对外数据响应处理
 *
 * @Created by ljp on 2019/11/14.
 */
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    private static final String SUCCESS_MESSAGE = "请求成功";

    /**
     * 根据自定义条件确认响应是否需要拦截处理
     */
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {

        // 如果在类上标注@IgnoreResponseAdvice注解，则此类不做统一响应处理
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }

        // 如果在方法上标注@IgnoreResponseAdvice注解，则此方法不做统一响应处理
        if (methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }

        return true;
    }

    /**
     * 在写入响应之前进行处理
     */
    @Nullable
    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(@Nullable Object data,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        CommonResponse<Object> response = new CommonResponse<>(0, SUCCESS_MESSAGE);

        // o 为响应对象
        if (data == null) {
            return response;
        }else if ( data instanceof CommonResponse) {
            return ((CommonResponse) data);
        }else {
            response.setData(data);
        }
        return response;
    }
}
