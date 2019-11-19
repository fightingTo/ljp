package com.cheer.ad.conf;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 自定义消息转换器（FastJson）
 *
 * @Created by ljp on 2019/11/14.
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();

        FastJsonHttpMessageConverter fjc = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);
        // 默认输出的map中的字段值如果有null，则不显示
        fjc.setFastJsonConfig(fastJsonConfig);
        converters.add(fjc);
        /*
         * 　1、WriteNullListAsEmpty  ：List字段如果为null,输出为[],而非null
         * 　2、WriteNullStringAsEmpty ： 字符类型字段如果为null,输出为"",而非null
         * 　3、DisableCircularReferenceDetect ：消除对同一对象循环引用的问题，默认为false（如果不配置有可能会进入死循环）
         * 　4、WriteNullBooleanAsFalse：Boolean字段如果为null,输出为false,而非null
         * 　5、WriteMapNullValue：是否输出值为null的字段,默认为false。
         */
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteNullListAsEmpty);
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteNullStringAsEmpty);
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteNullBooleanAsFalse);
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue);
    }
}
